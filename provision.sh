HOST=$1
USER=$2
PASSWORD=$3
PROJECT=$4
PREFIX=$5
R1_PREFIX=$6
R1_URL=$7
R2_PREFIX=$8
R2_URL=$9

oc login -u $USER -p $PASSWORD -n PROJECT $HOST --insecure-skip-tls-verify

oc new-project $PROJECT
oc project $PROJECT

oc replace --force  -f https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/72-1.2.GA/amq-broker-7-image-streams.yaml -n openshift

oc replace --force -n openshift -f \
https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/72-1.2.GA/amq-broker-7-scaledown-controller-image-streams.yaml

oc import-image amq-broker-72-openshift:1.2 -n openshift

oc import-image amq-broker-72-scaledown-controller-openshift:1.0 -n openshift

oc policy add-role-to-user view -z default

oc create secret generic amq-server-secret --from-file=keys/amq-certificates/broker.ks --from-file=keys/amq-certificates/broker.ts

oc process -f https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/72-1.2.GA/templates/amq-broker-72-persistence-ssl.yaml -p AMQ_KEYSTORE_PASSWORD=password -p AMQ_TRUSTSTORE_PASSWORD=password -p AMQ_SECRET=amq-server-secret -p AMQ_NAME=$PREFIX-broker | oc apply -f -

oc import-image amq-interconnect:latest -n openshift --from=registry.access.redhat.com/amq7/amq-interconnect --confirm

oc create secret generic inter-router-certs --from-file=tls.crt=keys/internal-certs/tls.crt  --from-file=tls.key=keys/internal-certs/tls.key  --from-file=ca.crt=keys/internal-certs/ca.crt

oc create secret generic client-ca --from-file=ca.crt=keys/client-certs/ca.crt

sleep 60

oc rsh broker-amq-0  ./$PREFIX-broker/bin/artemis address create --name=liquidityBalanceTransfers --multicast --no-anycast

oc rsh broker-amq-0  ./$PREFIX-broker/bin/artemis address create --name=accountUpdates --multicast --no-anycast

oc rsh broker-amq-0  ./$PREFIX-broker/bin/artemis queue create --address=accountUpdates --name=apac  --multicast --durable --silent
oc rsh broker-amq-0  ./$PREFIX-broker/bin/artemis queue create --address=accountUpdates --name=emea  --multicast --durable --silent
oc rsh broker-amq-0  ./$PREFIX-broker/bin/artemis queue create --address=accountUpdates --name=nam  --multicast --durable --silent

LOCAL_BROKER_URL=$PREFIX-broker-amq-headless.$PROJECT.svc

oc process -f ./router-template.yaml -p APPLICATION_NAME=$5-router -p REGIONAL_PREFIX=$5 -p LOCAL_BROKER_URL=$LOCAL_BROKER_URL \
    | sed 's/${REGIONAL_PREFIX}/'"$PREFIX"'/g' \
    | sed 's/${LOCAL_BROKER_URL}/'"$LOCAL_BROKER_URL"'/g'  \
    | sed 's/${R1_ROUTER_URL}/'"$R1_URL"'/g'  \
    | sed 's/${R2_ROUTER_URL}/'"$R2_URL"'/g'  \
    | sed 's/${R1_PREFIX}/'"$R1_PREFIX"'/g'  \
    | sed 's/${R2_PREFIX}/'"$R2_PREFIX"'/g'  \
    | oc apply -f -

oc rollout latest dc/$PREFIX-router