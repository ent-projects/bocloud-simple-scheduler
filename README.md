# bocloud-simple-scheduler


## 1. create token

```yaml
kubectl create -f https://raw.githubusercontent.com/kubesys/kubernetes-client/master/account.yaml
```
## 2. get token

```kubectl
kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep kubernetes-client | awk '{print $1}') | grep "token:" | awk -F":" '{print$2}' | sed 's/ //g'

```

## 3. run scheduler

set 'kubeUrl' and 'token' before running com.github.kubesys.bocloud.scheduler.Main
