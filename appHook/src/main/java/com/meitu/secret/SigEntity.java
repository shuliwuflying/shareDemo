package com.meitu.secret;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.lm.hook.utils.LogUtils;

public class SigEntity {
    private static final String sRequestUrl = "material/meimoji.json";
    private static final String[] sRequestParams =  new String[] {
            "1",
            "1",
            "46425204500897081217",
            "CN",
            "24",
            "1089867608",
            "xiaomi",
            "8.0",
            "d4:06:8c:35:47:02",
            "android",
            "1586487851",
            "oppo32",
            "WIFI",
            "zh-Hans",
            "1557549862",
            "1557549862"

    };
    private static final String sRequestVersion = "10003";


    public String finalString;
    public String sig;
    public String sigTime;
    public String sigVersion;


    public SigEntity(String paramString1, String paramString2, String paramString3) {
        this.sigTime = paramString1;
        this.sigVersion = paramString2;
        this.sig = paramString3;
        Log.e("sliver", "sigTime: "+sigTime);
        Log.e("sliver", "sigVersion: "+sigVersion);
        Log.e("sliver", "sig: "+sig);
    }

    public SigEntity(String paramString1, String paramString2, String paramString3, String paramString4) {
        this.sigTime = paramString1;
        this.sigVersion = paramString2;
        this.sig = paramString3;
        this.finalString = paramString4;

        Log.e("sliver", "sigTime: "+sigTime);
        Log.e("sliver", "sigVersion: "+sigVersion);
        Log.e("sliver", "sig: "+sig);
        Log.e("sliver", "finalString: "+finalString);
    }

    public static SigEntity generatorSig(Context context) {
        SigEntity sigEntity = generatorSig(sRequestUrl, sRequestParams, sRequestUrl, context);
        LogUtils.e("sliver", "sigEntity: "+sigEntity.toString());
        return sigEntity;
    }

    public static SigEntity generatorSig(@NonNull String paramString1, @NonNull String[] paramArrayOfString, @NonNull String paramString2) {
        if ((paramString1 != null) && (paramArrayOfString != null) && (paramString2 != null)) {
            byte[][] arrayOfByte = new byte[paramArrayOfString.length][];
            int i = 0;
            while (i < paramArrayOfString.length) {
                if (paramArrayOfString[i] == null) {
                    StringBuilder localStringBuilder = new StringBuilder();
                    localStringBuilder.append(paramString1);
                    localStringBuilder.append(" params[");
                    localStringBuilder.append(i);
                    localStringBuilder.append("] is null, encryption result by server maybe failed.");
                    Log.e("SigEntity", localStringBuilder.toString());
                    paramArrayOfString[i] = "";
                }
                arrayOfByte[i] = paramArrayOfString[i].getBytes();
                i += 1;
            }
            return nativeGeneratorSigOld(paramString1, arrayOfByte, paramString2);
        }
        return null;
    }

    public static SigEntity generatorSig(@NonNull String paramString1, @NonNull String[] paramArrayOfString, @NonNull String paramString2, @NonNull Object paramObject) {
        byte[][] arrayOfByte = null;
        if ((paramString1 != null) && (paramArrayOfString != null) && (paramString2 != null) && (paramObject != null)) {
            if ((paramObject instanceof Context)) {
                arrayOfByte = new byte[paramArrayOfString.length][];
                int i = 0;
                while (i < paramArrayOfString.length) {
                    if (paramArrayOfString[i] == null) {
                        StringBuilder localStringBuilder = new StringBuilder();
                        localStringBuilder.append(paramString1);
                        localStringBuilder.append(" params[");
                        localStringBuilder.append(i);
                        localStringBuilder.append("] is null, encryption result by server maybe failed.");
                        Log.e("SigEntity", localStringBuilder.toString());
                        paramArrayOfString[i] = "";
                    }
                    arrayOfByte[i] = paramArrayOfString[i].getBytes();
                    i += 1;
                }
            }
        }
        return nativeGeneratorSig(paramString1, arrayOfByte, paramString2, paramObject);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sigTime: "+sigTime+"\n");
        sb.append("sigVersion: "+sigVersion+"\n");
        sb.append("sig: "+sig+"\n");
        sb.append("finalString: "+finalString);
        return sb.toString();
    }

    public static native SigEntity nativeGeneratorSig(String paramString1, byte[][] paramArrayOfByte, String paramString2, Object paramObject);

    public static native SigEntity nativeGeneratorSigFinal(String paramString1, byte[][] paramArrayOfByte, String paramString2, Object paramObject);

    public static native SigEntity nativeGeneratorSigOld(String paramString1, byte[][] paramArrayOfByte, String paramString2);
}
