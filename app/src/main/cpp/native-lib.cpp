#include <jni.h>
#include <string>

//声明在bspatch.c
extern "C" {
    extern int p_main(int argc,const char* argv[]);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_zhoumohan_luckymorning_main_MainActivity_bsPath(JNIEnv *env, jobject instance,
                                                                 jstring oldApk_, jstring newApk_,
    // 将java字符串转为c++字符串， 转为UTF-8格式的char指针
                                                                jstring outputPath_) {
    const char *oldApk = env->GetStringUTFChars(oldApk_, 0);
    const char *newApk = env->GetStringUTFChars(newApk_, 0);
    const char *outputPath = env->GetStringUTFChars(outputPath_, 0);

    //合成
    const char* argv[] = {"",oldApk,outputPath,newApk};
    p_main(4,argv);

    // 释放指向Unicode格式的Char指针

    env->ReleaseStringUTFChars(oldApk_, oldApk);
    env->ReleaseStringUTFChars(newApk_, newApk);
    env->ReleaseStringUTFChars(outputPath_, outputPath);
}