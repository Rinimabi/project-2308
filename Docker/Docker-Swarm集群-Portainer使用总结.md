# 一、安装docker
## 1.windows7下安装docker
因为windows7没有Hyper-V，所以并不能使用docker for windows，但是可以使用docker toolbox，也有人喜欢放到linux虚拟机里用（docker for windows也是带了linux内核）。

点击[Docker Toolbox overview](https://jff--docker-docs.netlify.app/toolbox/overview/)下载。

安装完成后，如果你之前安装过git，那么打开Docker Quickstart Terminal可能会报错“bush无法找到”，你需要点击Docker Quickstart Terminal右键属性，手动改成你Git目录下的bush.exe。

![图1-1](https://img-blog.csdnimg.cn/20200220104129675.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzI5NTU5ODE5,size_16,color_FFFFFF,t_70)

打开后初始化需要下载boot2docker.iso，必定龟速，可以手动下载[boot2docker.iso](https://github.com/boot2docker/boot2docker/releases)。复制文件boot2docker.iso到C:\Users\mid1507\.docker\machine\cache\boot2docker.iso目录，重新打开Docker Quickstart Terminal。

详见：[Docker的boot2docker.iso镜像使用](https://blog.csdn.net/shanshan_blog/article/details/70242051?utm_source=blogxgwz6)  [win7搭建docker解决的一些坑](https://www.daixiaorui.com/read/277.html)

这时候就已经可以在win7下使用docker了。

注意，Docker Quickstart Terminal的主机并不是windows，而是它自己构建的一个linux系统，所以你并不能通过localhost访问docker容器，得访问这个内置Linux的IP，一般情况下这个地址是192.168.99.100。

![图1-2](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjM3MDM4LzIwMTkwNC82MzcwMzgtMjAxOTA0MDkxMDAzMDg1MTEtMTE3OTIxNTQzNy5wbmc?x-oss-process=image/format,png)

另见[解决windows系统无法对docker容器进行端口映射的问题](https://www.cnblogs.com/hypnus-ly/p/8683215.html)

## 2.windows10下安装docker
这就比较简单，直接下载安装，没遇到什么坑。[Docker Desktop for Windows](https://hub.docker.com/editions/community/docker-ce-desktop-windows/)

## 3.Linux下安装docker
采用yum安装的话，直接yum search docker就可以找到了。因为生产上多用的是centos7，这里给一下aliyun的镜像。

新建docker-ce.repo
```ini
[docker-ce-stable]
name=Docker CE Stable - $basearch
baseurl=https://mirrors.aliyun.com/docker-ce/linux/centos/7/$basearch/stable
enabled=1
gpgcheck=1
gpgkey=https://mirrors.aliyun.com/docker-ce/linux/centos/gpg
```
然后操作一下：

```ini
cp docker-ce.repo /etc/yum.repos.d/docker-ce.repo

yum makecache

yum install docker-ce -y

systemctl enable docker #开机自启docker

systemctl daemon-reload #重启docker

systemctl restart docker
```

## 4.配置守护进程daemon.json

```ini
vi /etc/docker/daemon.json
```
如果搭建了自己的docker仓库或者使用他人的仓库，需要在这里添加上去。
```ini
{
    "insecure-registries": [""]
}
```

## 5.配置VSCODE使用
搜索官方扩展插件Docker，装好之后，进入个人配置中，搜索docker，装好后就可以用了。
Windows7的话在这之后你就不需要Docker Quickstart Terminal了，打开Oracle VM VirtualBox虚拟机启动linux就可以了。
Windows10和Linux下开箱即食，简单配置下就好。

![图1-3](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjM3MDM4LzIwMTkwNC82MzcwMzgtMjAxOTA0MDkxMDE4Mzg3ODktMTUwNjY5MDQ0Mi5wbmc?x-oss-process=image/format,png)

# 二、Docker的基本使用
## 1.docker基本命令
```ini
docker search rabbitmq # 查找仓库中有关rabbitmq的镜像。
 
docker pull 3.7.7-management # 可以根据tag也可以根据image id进行下载。
 
docker tag rabbitmq：3.7.7 rabbitmq:test   # tag
 
docker save -o /opt/rabbitmq.tar  3.7.7-management # 保存镜像
 
docker images # 查看已下载的镜像。
 
# 启动一个容器，运行镜像
# -d   后台运行容器；
# --name   指定容器名；
# -p   指定服务运行的端口（5672：应用访问端口；15672：控制台Web端口号）；
# df80af9ca0c9   镜像ID
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672  df80af9ca0c9
 
docker ps # 查看正在运行容器
 
docker port rabbitmq # 查看容器rabbitmq的网络情况
 
docker stop rabbitmq # 关闭容器rabbitmq
 
docker rm rabbitmq # 删除容器rabbitmq
 
docker rmi XXX # 按镜像ID删除镜像
 
docker build -t XXX . # 将项目制作成一个镜像（需要dockerfile文件支持）
```

## 2.Linux使用yum如何卸载Docker
```ini
systemctl stop docker.service

yum list installed|grep docker 

yum –y remove docker.x86_64

yum –y remove docker-client.x86_64 

yum –y remove docker-common.x86_64 

rm -rf /var/lib/docker 
```
## 3.Docker的重启
```ini
systemctl start docker # 启动

sudo systemctl daemon-reload # 守护进程重启
 
systemctl restart  docker # 重启docker服务
 
sudo service docker restart # 重启docker服务
 
service docker stop # 关闭docker
 
systemctl stop docker # 关闭docker
```
## 4.制作镜像
首先需要写一个Dockerfile

![图2-1](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjM3MDM4LzIwMTkwNC82MzcwMzgtMjAxOTA0MTAwOTMxNDg2MTMtNzMwNzY5MjcucG5n?x-oss-process=image/format,png)

之后进入Dockerfile所在文件夹就可以用 docker build t todolist-service:master . （注意后面的点）目录生成镜像了。jar包比较简单，如果是前端项目，涉及到路径问题，必须在Linux下将前端项目打包，然后再制作镜像。

## 5.部署Docker Swarm集群
很简单，使用docker swarm init 命令就可以创建集群，让其他机器加入就可以。注意防火墙，因为各个机器之间需要进行通信。当然Swarm还有很多概念，Service、Stack、Overlay等等。

创建一个service
```shell
docker service create --name demo busybox sh -c "while true;do sleep 3600;done"
```
因为不确定创建的容器运行在哪个节点主机上面，所以需要保证不同主机之间的容器能够通信，这就需要创建一个overlay的网络。(其实还有很多坑，自行谷歌。)

创建一个overlay网络

```shell
docker network create -d overlay demo
```

# 三、疑难技巧
## 1.如何删除Docker Swarm中的Node
```shell
Docker node ls
Docker node demote
Docker swarm leave
Docker node remove
```

## 2.如何解决docker集群节点的容器无法访问网络/互相访问的问题
```ini
# docker info查看是否有报错，如果有以下：
WARNING: IPv4 forwarding is disabled
WARNING: bridge-nf-call-iptables is disabled
WARNING: bridge-nf-call-ip6tables is disabled
vim /etc/sysctl.conf
# 配置转发
net.ipv4.ip_forward=1
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
# 重启服务，让配置生效
systemctl restart network
```

## 3.修改了本机IP，无法访问Docker
重启Linux或者systemctl restart network就好。

## 4.进入docker容器并复制文件
```ini
# 进入容器内部环境
docker exec -it 775c7c9ee1e1 /bin/bash  
# 退出容器
exic
# 将容器内的jar包复制到Linux /opt路径下
docker cp 4a1c4d6a18ca:/usr/local/ly-cloud-config-server-0.0.1-SNAPSHOT.jar /opt
```

## 5.如何在已部署的集群添加节点？
在集群Manage节点下执行命令行：
```shell
 docker swarm join-token worker 
```
随后得到一个命令行：

![图2-2](https://img-blog.csdnimg.cn/20200220112510481.png)

将这行命令复制到你要添加集群的机器上运行，随后在portiner的swarm中就可以看到，机器已经加入了集群。
如何更改节点的名称？直接更改hostname：
```shell
hostnamectl set-hostname p1

service docker restart
```
# 四、使用Portainer图形界面管理Docker
安装portainer，先写一个portainer.yml文件（Docker Stack暂且不表）。
```ini
version: '3.2'
 
services:
  agent:
    image: portainer/agent
    environment:
      AGENT_CLUSTER_ADDR: tasks.agent
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
      - /var/lib/docker/volumes:/var/lib/docker/volumes
    networks:
      - agent_network
    deploy:
      mode: global # 设置global模式之后加入集群的机器就可以自动创建agent
      replicas: 1 # 有多少台机器就写几个
      placement:
        constraints: [node.platform.os == linux]
 
  portainer:
    image: portainer/portainer
    command: -H tcp://tasks.agent:9001 --tlsskipverify
    ports:
      - "9000:9000"
    volumes:
      - /opt/portainer_data:/data
    networks:
      - agent_network
    deploy:
      mode: replicated
      replicas: 1
      placement:
        constraints: [node.role == manager]
 
networks:
  agent_network:
    driver: overlay # 创造一个networks用于集群中机器的通信
    attachable: true
 
volumes:
  portainer_data:
```
然后创造集群，安装portainer，让机器都加入，就可以了。

```ini
docker swarm init #创建集群
 
docker stack deploy --compose-file=portainer-agent-stack.yml portainer # 安装portainer
 
docker node update --label-add type=master p1 # 将主机标签改为p1
```

然后你打开9000端口就可以看到登录界面了，第一次登录需要创建一个初始账号，我一般设为admin/admin。

这就是docker三剑客的使用方法了。使用Docker Swarm创造集群，使用Docker Stack完成 Docker 集群环境下的多服务编排。

Docker Stack创造Docker Service，然后启动一个容器。

![图2-3](https://img-blog.csdnimg.cn/20200220122215643.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzI5NTU5ODE5,size_16,color_FFFFFF,t_70)

在这里你就可以对docker进行图形界面化管理了。

