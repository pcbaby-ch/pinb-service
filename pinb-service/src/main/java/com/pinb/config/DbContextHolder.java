package com.pinb.config;

/**
 * 
 * @author chenzhao @date Apr 9, 2019
 */
public class DbContextHolder {

    public enum  DbType {
        WRITE,
        READONLY;
    }

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();

    public static void setDbType(DbType dbType) {
        if(dbType == null){
            throw new NullPointerException();
        }
        contextHolder.set(dbType);
    }

    public static DbType getDbType() {
        return contextHolder.get()== null ? DbType.WRITE : contextHolder.get();
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}
