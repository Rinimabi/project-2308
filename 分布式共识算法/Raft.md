[toc]

# Raft

## 三种成员身份

raft提供三种成员身份，**领导者（leader）、跟随者（follower）、候选人（candidate）**

- 跟随者：相当于paxos中的acceptor，接收和处理leader的消息，当leader故障时，主动推荐自己为候选人
- 候选人：向其他节点发送请求投票消息（Request Vote），如果获得大多数选票，则晋升为leader
- 领导者：处理写请求，管理日志复制、发送心跳消息。
