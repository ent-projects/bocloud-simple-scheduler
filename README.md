# bocloud-simple-scheduler

# 我们基于自研的Kubernetes-client进行开发，请参照 [kubernetes-client](https://github.com/kubesys/kubernetes-client) 

## 1. 创建token

```yaml
kubectl create -f https://raw.githubusercontent.com/kubesys/kubernetes-client/master/account.yaml
```
## 2. 获取token

```kubectl
kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep kubernetes-client | awk '{print $1}') | grep "token:" | awk -F":" '{print$2}' | sed 's/ //g'

```