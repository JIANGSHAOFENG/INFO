package com.dbms_v2;

import android.app.Application;

public class MyApplication extends Application { 
// �����˳���� 
private static boolean isProgramExit = false; 
public void setExit(boolean exit) { 
isProgramExit = exit; 
} 
public boolean isExit() { 
return isProgramExit; 
} 
} 