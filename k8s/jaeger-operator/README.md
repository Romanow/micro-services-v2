# Jaeger Operator

```shell
# устанавливаем репозиторий с cert manager
$ helm repo add jetstack https://charts.jetstack.io  
$ helm repo update

# устанавливаем cert manager CRD
$ kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.10.0/cert-manager.crds.yaml

# устанавливаем cert manager
$ helm install cert-manager jetstack/cert-manager \
    --namespace cert-manager \
    --create-namespace \
    --version v1.10.0

# устанавливаем репозиторий с jaeger
$ helm repo add jaegertracing https://jaegertracing.github.io/helm-charts

# устанавливаем Jaeger Operator
$ helm install jaeger-operator jaegertracing/jaeger-operator \
    --values=deploy-values.yml \
    --create-namespace \
    -n tracing
```