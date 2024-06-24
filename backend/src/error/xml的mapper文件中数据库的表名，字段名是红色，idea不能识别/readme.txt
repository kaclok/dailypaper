可能和mysql在docker中安装有关系。
截图1中点击1处的数据库标记，就能正常访问识别数据库，但是按照ip形式访问数据库就会存在报错：[HY000][1130] null, message from server: "Host 'host.docker.internal' is not allowed to connect to this MySQL server".

但是无论是截图1中哪种方式，代码运行的时候访问数据库是正常的。