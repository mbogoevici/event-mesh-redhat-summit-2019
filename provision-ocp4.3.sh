./provision.sh https://api.$1:6443 $2 $3 nam  nam  apac apac-router-apac.apps.$1 emea emea-router-emea.apps.$1
./provision.sh https://api.$1:6443 $2 $3 apac apac nam   nam-router-nam.apps.$1  emea emea-router-emea.apps.$1
./provision.sh https://api.$1:6443 $2 $3 emea emea nam   nam-router-nam.apps.$1  apac apac-router-apac.apps.$1

cd core-banking

./mvnw clean package
# ./mvnw clean fabric8:deploy -Pnam -Dfabric8.username=$2 -Dfabric8.password=$3 -Dfabric8.namespace=nam
oc project nam
oc new-build --binary=true --name=core-banking registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.5
oc start-build core-banking --from-file=target/core-banking-0.0.1-SNAPSHOT.jar --follow
oc new-app core-banking -e CONNECT_URL=nam-router.nam.svc -e REGION_NAME=apac -e SERVER_PORT=8080
oc expose service core-banking


# ./mvnw clean fabric8:deploy -Papac -Dfabric8.username=$2 -Dfabric8.password=$3 -Dfabric8.namespace=apac
oc project apac
oc new-build --binary=true --name=core-banking registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.5
oc start-build core-banking --from-file=target/core-banking-0.0.1-SNAPSHOT.jar --follow
oc new-app core-banking -e CONNECT_URL=apac-router.apac.svc -e REGION_NAME=apac -e SERVER_PORT=8080
oc expose service core-banking

# ./mvnw clean fabric8:deploy -Pemea -Dfabric8.username=$2 -Dfabric8.password=$3 -Dfabric8.namespace=emea
oc project emea
oc new-build --binary=true --name=core-banking registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.5
oc start-build core-banking --from-file=target/core-banking-0.0.1-SNAPSHOT.jar --follow
oc new-app core-banking -e CONNECT_URL=emea-router.emea.svc -e REGION_NAME=apac -e SERVER_PORT=8080
oc expose service core-banking
