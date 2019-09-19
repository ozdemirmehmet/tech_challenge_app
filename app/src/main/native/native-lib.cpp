//
// Created by mozdemir on 6/26/2018.
//

#include <jni.h>

extern "C" {
JNIEXPORT jstring JNICALL
Java_com_iamozdemir_techchallengeapp_activities_LoginActivity_getKeys(JNIEnv *env,
                                                                      jobject instance) {

    return env->NewStringUTF("{\"loginUsername\":\"kariyer\",\"loginPassword\":\"2019ADev\"}");
}
}