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

oc project $PROJECT

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