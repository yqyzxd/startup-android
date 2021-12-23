package com.wind.startup;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

public class ProcessUtil {

    public static boolean isMainProcess(Context context) {
        if (context == null) {
            return false;
        } else {
            String pkgName = context.getApplicationContext().getPackageName();
            String processName = getProcessName(context);
            return pkgName.equals(processName);
        }
    }

    public static String getProcessName(Context context) {
        String processName;
        if ((processName = getProcessFromFile()) == null) {
            processName = getProcessNameByAM(context);
        }

        return processName;
    }
    private static String getProcessFromFile() {
        BufferedReader bufferedReader=null;
        try {
            String fileName="/proc/"+Process.myPid()+"/cmdline";
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "iso-8859-1"));
            StringBuilder processNameBuilder = new StringBuilder();
            int read=-1;
            while ((read=bufferedReader.read())>0){
                processNameBuilder.append((char) read);
            }
            //System.out.println("ProcessUtil getProcessFromFile:"+processNameBuilder.toString());
            return processNameBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }
    private static String getProcessFromFile2() {
        //System.out.println("ProcessUtil getProcessFromFile");
        BufferedReader var0 = null;
        boolean var8 = false;

        String var14;
        label97: {
            try {
                var8 = true;
                int var1 = Process.myPid();
                var14 = "/proc/" + var1 + "/cmdline";
                var0 = new BufferedReader(new InputStreamReader(new FileInputStream(var14), "iso-8859-1"));
                StringBuilder var2 = new StringBuilder();

                while((var1 = var0.read()) > 0) {
                    var2.append((char)var1);
                }

                var14 = var2.toString();
                var8 = false;
                break label97;
            } catch (Exception var12) {
                var8 = false;
            } finally {
                if (var8) {
                    if (var0 != null) {
                        try {
                            var0.close();
                        } catch (IOException var9) {
                            var9.printStackTrace();
                        }
                    }

                }
            }

            if (var0 != null) {
                try {
                    var0.close();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }
            }

            return null;
        }

        try {
            var0.close();
        } catch (IOException var11) {
            var11.printStackTrace();
        }

        return var14;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名

    private static String getProcessName(int pid) {
    BufferedReader reader = null;
    try {
    reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
    String processName = reader.readLine();
    if (!TextUtils.isEmpty(processName)) {
    processName = processName.trim();
    }
    return processName;
    } catch (Throwable throwable) {
    throwable.printStackTrace();
    } finally {
    try {
    if (reader != null) {
    reader.close();
    }
    } catch (IOException exception) {
    exception.printStackTrace();
    }
    }
    return null;
    } */
    private static String getProcessNameByAM(Context context) {
        //System.out.println("ProcessUtil getProcessNameByAM");
        String processName = null;
        ActivityManager am;
        if ((am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)) == null) {
            return null;
        } else {
            while(true) {
                List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos;
               // List var2;
                if ((runningAppProcessInfos = am.getRunningAppProcesses()) != null) {
                    Iterator iterator = runningAppProcessInfos.iterator();

                    while(iterator.hasNext()) {
                        ActivityManager.RunningAppProcessInfo processInfo;
                        if ((processInfo = (ActivityManager.RunningAppProcessInfo)iterator.next()).pid == Process.myPid()) {
                            processName = processInfo.processName;
                            break;
                        }
                    }
                }

                if (!TextUtils.isEmpty(processName)) {
                    return processName;
                }

                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var4) {
                    var4.printStackTrace();
                }
            }
        }
    }

}
