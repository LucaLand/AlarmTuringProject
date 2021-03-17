package org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CategoryFilter implements Filter {
    private List<String> listCategories;
    private String alias = "";




    public CategoryFilter(String categories) {
        this.listCategories = createCategories(categories);
    }

    public CategoryFilter(String categories, String alias) {
        this.listCategories = createCategories(categories);
        this.alias = alias;
    }

    private List<String> createCategories(String categories){
        List<String> categoriesList = new LinkedList<>();
        Scanner s = new Scanner(categories);
        s.useDelimiter(",|;");

        while(s.hasNext()){
            categoriesList.add(s.next());
        }

        return categoriesList;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public boolean categoryCheck(String s) {
        for(String category : listCategories){
            if(s.equals(category))
                return true;
        }
        return false;
    }
}
