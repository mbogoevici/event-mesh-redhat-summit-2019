./provision.sh https://master00-$1.generic.opentlc.com $2 $3 nam nam apac  apac-router-apac.apps-$1.generic.opentlc.com emea emea-router-emea.apps-$1.generic.opentlc.com
./provision.sh https://master00-$1.generic.opentlc.com $2 $3 apac apac nam  nam-router-nam.apps-$1.generic.opentlc.com emea emea-router-emea.apps-$1.generic.opentlc.com
./provision.sh https://master00-$1.generic.opentlc.com $2 $3 emea emea nam  nam-router-nam.apps-$1.generic.opentlc.com apac apac-router-apac.apps-$1.generic.opentlc.com

cd core-banking

./mvnw clean fabric8:deploy -Pnam -Dfabric8.username=$2 -Dfabric8.password=$3 -Dfabric8.namespace=nam
./mvnw clean fabric8:deploy -Papac -Dfabric8.username=$2 -Dfabric8.password=$3 -Dfabric8.namespace=apac
./mvnw clean fabric8:deploy -Pemea -Dfabric8.username=$2 -Dfabric8.password=$3 -Dfabric8.namespace=emea
