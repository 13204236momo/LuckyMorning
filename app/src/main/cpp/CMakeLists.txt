
cmake_minimum_required(VERSION 3.4.1)

#查找文件系统中指定模式的路径
file(GLOB bzip_source ${CMAKE_SOURCE_DIR}/bzip/*.c)
#设置本地动态库，编译生成动态库
add_library(
        #模块名
        native-lib
        #动态库、分享库
        SHARED
        #源文件
        native-lib.cpp
        ${bzip_source}
        bspatch.c)

#查找系统库，日志输出文件log
find_library(
        log-lib

        log)
#需要连接或者编译库
target_link_libraries(
        native-lib

        ${log-lib})