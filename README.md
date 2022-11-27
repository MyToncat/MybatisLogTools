# Qtools
## 开发目的
目前常用框架是SpringBoot + Mybatis/Mp , 日志输出的SQL是预编译的，需要人工去填写参数，若是SQL很复杂，且参数很多，是比较费时间的。虽然Idea有免费的Mybatis日志格式化插件。但是存在版本兼容问题，若是Eclipse的话，那更是麻烦。所以开发一款Qtool工具

## 目前功能
目前已有功能
- MybatisLog相关
  - Mybatis SQL自动组装并格式化，支持高亮
  - 对已有SQL的格式化，高亮显示
- Json相关
  - Json格式化，高亮


其他功能：
- Json 按Key自然排序，比对 （未实现）
- 报文比对
等等

后续慢慢添加


## 环境：
- Idea 2020.2.3
- Java 1.8
- Gradle 7.1
- Swing

## 编译
- 拉取代码，安装Java 和 Gradle 环境
- 执行 gradle build  编译打包
