package com.brucezee.jspider.samples;

import java.util.List;

/**
 * 解析的最终结果对象
 * Created by brucezee on 2017/09/30.
 */
public class ResultObject {
    private Store store;
    private MixInfo mixInfo;
    private Integer expensive;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public MixInfo getMixInfo() {
        return mixInfo;
    }

    public void setMixInfo(MixInfo mixInfo) {
        this.mixInfo = mixInfo;
    }

    public Integer getExpensive() {
        return expensive;
    }

    public void setExpensive(Integer expensive) {
        this.expensive = expensive;
    }

    static class MixInfo {
        private String factoryName;
        private String storeName;
        private String address;
        private String channelCode;

        public String getChannelCode() {
            return channelCode;
        }

        public void setChannelCode(String channelCode) {
            this.channelCode = channelCode;
        }

        public String getFactoryName() {
            return factoryName;
        }

        public void setFactoryName(String factoryName) {
            this.factoryName = factoryName;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    static class Store {
        private String manager;
        private Bicycle bicycle;
        private List<Book> book;

        public String getManager() {
            return manager;
        }

        public void setManager(String manager) {
            this.manager = manager;
        }

        public Bicycle getBicycle() {
            return bicycle;
        }

        public void setBicycle(Bicycle bicycle) {
            this.bicycle = bicycle;
        }

        public List<Book> getBook() {
            return book;
        }

        public void setBook(List<Book> book) {
            this.book = book;
        }
    }

    static class Book {
        private String author;
        private String category;
        private String title;
        private String isbnNo;
        private Double price;
        private String cityCode;
        private Double baseAmount;
        private String createTime;

        public Double getBaseAmount() {
            return baseAmount;
        }

        public void setBaseAmount(Double baseAmount) {
            this.baseAmount = baseAmount;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIsbnNo() {
            return isbnNo;
        }

        public void setIsbnNo(String isbnNo) {
            this.isbnNo = isbnNo;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

    static class Bicycle {
        private String color;
        private Double price;
        private String bicycleStoreManager;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getBicycleStoreManager() {
            return bicycleStoreManager;
        }

        public void setBicycleStoreManager(String bicycleStoreManager) {
            this.bicycleStoreManager = bicycleStoreManager;
        }
    }
}
