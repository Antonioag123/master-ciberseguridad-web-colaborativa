package helpers;

import play.Play;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CheatSheetHelper {
    private static final File cheatSheetBaseDir = new File(Play.frameworkPath, "documentation/cheatsheets");

    public static File[] getSheets(String category, String docLang) {
        String docLangDir = (docLang != null && (!"en".equalsIgnoreCase(docLang) && !docLang.matches("en-.*"))) ? "_" + docLang : "";
        File cheatSheetDir = new File(cheatSheetBaseDir + docLangDir, category);

        if (!cheatSheetDir.exists()){
            cheatSheetDir = new File(cheatSheetBaseDir, category);
        }

        if (cheatSheetDir.exists() && cheatSheetDir.isDirectory()) {
            File[] sheetFiles = cheatSheetDir.listFiles(pathname -> pathname.isFile() && pathname.getName().endsWith(".textile"));

            // first letters of file name before "-" serves as sort index
            Arrays.sort(sheetFiles, (f1, f2) -> {
                String o1 = f1.getName();
                String o2 = f2.getName();

                if (o1.contains("-") && o2.contains("-")) {
                    return o1.substring(0, o1.indexOf("-"))
                            .compareTo(o2.substring(0, o1.indexOf("-")));
                } else {
                    return o1.compareTo(o2);
                }
            });

            return sheetFiles;
        }

        return null;
    }

    public static String getCategoryTitle(String category) {
        // split camelCaseWord into separate words
        String[] parts = category.trim().split("(?<!^)(?=[A-Z])");
        StringBuilder title = new StringBuilder();

        // capitalize first char of each word
        for (String part : parts) {
            if (part.length() > 0) {
                title.append(Character.toUpperCase(part.charAt(0)));

                if (part.length() > 1) {
                    title.append(part.substring(1));
                }
                title.append(" ");
            }
        }

        return title.toString().trim();
    }

    public static Map<String, String> listCategoriesAndTitles(String docLang) {
        String docLangDir = (docLang != null && (!"en".equalsIgnoreCase(docLang) && !docLang.matches("en-.*"))) ? "_" + docLang : "";
        File[] categories = new File(cheatSheetBaseDir + docLangDir).listFiles(File::isDirectory);

        if(categories==null || categories.length<=0){
            categories = cheatSheetBaseDir.listFiles(File::isDirectory);
        }

        Arrays.sort(categories);

        Map<String, String> categoriesAndTitles = new LinkedHashMap<String, String>();

        for (File category : categories) {
            categoriesAndTitles.put(category.getName(), getCategoryTitle(category.getName()));
        }

        return categoriesAndTitles;
    }
}
