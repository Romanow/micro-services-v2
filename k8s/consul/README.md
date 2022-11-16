# Consul Service Mesh

## Add external service to k8s cluster

### Install Consul in k8s cluster

```shell
$ helm repo add hashicorp https://helm.releases.hashicorp.com
$ helm install consul hashicorp/consul --values consul.yml
$ kubectl label namespace default connect-inject=enabled
```

### Run and configure VM

```shell
$ cd vm/
$ terrafrom apply -auto-approve
```

#### Install kubectl

```shell
$ curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
$ sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
$ kubectl version --client
```

#### Install doctl

```shell
$ sudo snap install doctl
$ sudo snap connect doctl:kube-config
$ doctl auth init
$ doctl kubernetes cluster kubeconfig save k8s-cluster
```

#### Install and configure Postgres

```shell
$ sudo sh -c 'echo "deb https://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'

$ wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add - $ sudo apt-get update

$ sudo apt-get -y install postgresql-13

$ sudo su postgres

$ psql <<EOF
CREATE ROLE program WITH PASSWORD 'test';
ALTER ROLE program WITH LOGIN;
CREATE DATABASE simple_backend;
GRANT ALL PRIVILEGES ON DATABASE simple_backend TO program;
EOF

# listen external address (listen_addresses = '*')
$ sudo nano /etc/postgresql/13/main/postgresql.conf

# allow external connections (host all all 0.0.0.0/0 md5)
$ sudo nano /etc/postgresql/13/main/pg_hba.conf

$ sudo systemctl restart postrges

$ psql -h 10.110.0.6 -p 5432 -U program simple_backend
```

### Connect VM to Consul Cluster

Проверяем что VM в том же VPC, что и k8s cluster, берем private address. Подключаем Consul Client к Consul Cluster.

```shell
$ consul agent \
    -advertise="10.110.0.6" \
    -retry-join='provider=k8s host_network=true label_selector="app=consul,component=server"' \
    -bind=10.110.0.6  \
    -hcl='leave_on_terminate = true' \
    -hcl='ports { grpc = 8502 }' \
    -data-dir=/tmp/consul
```

Регистрируем postgres в Consul:

```hcl
service {
  name = "postgres-service"
  port = 5432
  tags = ["primary"]
}
```

```shell
$ consul services register postgres.hcl
```

## Configure CoreDNS to resolve .consul

```shell
$ kubectl get svc consul-dns --output jsonpath='{.spec.clusterIP}'
10.245.161.203

$ kubectl edit configmap coredns --namespace kube-system
apiVersion: v1
kind: ConfigMap
metadata:
  labels:
    addonmanager.kubernetes.io/mode: EnsureExists
  name: coredns
  namespace: kube-system
data:
  Corefile: |
    .:53 {
        <Existing CoreDNS definition>
    }
   consul {
     errors
     cache 30
     forward . 10.245.161.203
   }
```

1. [Resolve Consul DNS Requests in Kubernetes](https://developer.hashicorp.com/consul/docs/k8s/dns)