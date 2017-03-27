package com.brucezee.jspider.berkeley;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.*;

import java.io.File;

/**
 * BDB数据库环境,可以缓存StoredClassCatalog并共享
 * Created by brucezee on 2017/1/13.
 */
public class BdbEnvironment extends Environment {
    private Database classCatalogDB;
    private StoredClassCatalog classCatalog;

    /**
     * 构造方法
     * @param envHome 数据库环境目录
     * @param envConfig config options  数据库换纪念馆配置
     * @throws DatabaseException
     */
    public BdbEnvironment(File envHome, EnvironmentConfig envConfig) throws DatabaseException {
        super(envHome, envConfig);
    }

    /**
     * 返回StoredClassCatalog
     * @return the cached class catalog
     */
    public StoredClassCatalog getClassCatalog() {
        if(classCatalog == null) {
            DatabaseConfig dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            try {
                classCatalogDB = openDatabase(null, "classCatalog", dbConfig);
                classCatalog = new StoredClassCatalog(classCatalogDB);
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        }
        return classCatalog;
    }

    @Override
    public synchronized void close() throws DatabaseException {
        if(classCatalogDB != null) {
            classCatalogDB.close();
        }
        super.close();
    }

}