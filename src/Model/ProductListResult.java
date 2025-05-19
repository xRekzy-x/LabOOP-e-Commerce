package Model;

import java.util.ArrayList;
import java.util.List;

import Controller.DataControl;

public class ProductListResult {
    private List<Product> ProductEachPage = new ArrayList<>();
    public ProductListResult(){}
    public String toString(){
        StringBuilder content= new StringBuilder();
        for(int i =0;i<ProductEachPage.size();i++){
            content.append("\n ("+(i+1)+") "+ProductEachPage.get(i).toString());
        }
        //content.append("\n"+getCurrentPage()+"/"+getTotalPages());
        return content.toString();
    }
    public Product getProduct(String productName){
        for(Product product : ProductEachPage){
            if(product.getProName().equals(productName))
                return product;
        }//ITERATOR
        return null;
    }
    public Product getProductByID(String productID){
        for(Product product : ProductEachPage){
            if(product.getProId().equals(productID))
                return product;
        }//ITERATOR
        return null;
    }
    public List<Product> getAllProductsEachPage(){
        return ProductEachPage;
    }
    public int getCurrentPage(){
        List<Product> allProducts = DataControl.readAllProducts();
        for(int i =0;i<allProducts.size();i++){
            if((allProducts.get(i).getProId()).equals((ProductEachPage.get(0).getProId()))){
                return i/10+1;
            }
        }
        return 0;
    }
    public int getTotalPages(){
        int size = DataControl.readAllProducts().size();
        if(size%10==0) return (size/10);
        else return (size/10+1);
    }
    
}
