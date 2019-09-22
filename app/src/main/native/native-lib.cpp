/*
 * Created by Mehmet Ozdemir on 9/18/19 12:04 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/18/19 11:58 AM
 */

#include <jni.h>

extern "C" {
JNIEXPORT jstring JNICALL
Java_com_iamozdemir_techchallengeapp_activities_LoginActivity_getKeys(JNIEnv *env,
                                                                      jobject instance) {

    return env->NewStringUTF("{\"loginUsername\":\"kariyer\",\"loginPassword\":\"2019ADev\"}");
}
}