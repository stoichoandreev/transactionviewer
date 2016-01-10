package test.badoo.sniper.badootest.models;

import java.io.Serializable;

/**
 * Created by sniper on 1/9/16.
 */
public class Product implements Serializable{
    private String name;
    private int transactionNumbers;

    public Product(){}

    public Product(String name,int count){
        this.name = name;
        this.transactionNumbers = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTransactionNumbers() {
        return transactionNumbers;
    }

    public void setTransactionNumbers(int transactionNumbers) {
        this.transactionNumbers = transactionNumbers;
    }
    public void incrementTransactionNumber(){
        transactionNumbers++;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
