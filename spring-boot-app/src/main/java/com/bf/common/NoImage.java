package com.bf.common;

public enum NoImage {
    
    MASSAGE_CHAIR("no_image_small_m.png"),
    LACLOUD("no_image_small_l.png"),
    W_WATER("no_image_small_w.png");
    
    private final String imagePath;
    
    private NoImage(String imagePath) {
        this.imagePath = imagePath;
    }

    public String imagePath() {
        return String.format("/images/%s", imagePath);
    }
    
    public static String findImage(String grpCode) {
     
        switch (grpCode) {
            case "M":
                return MASSAGE_CHAIR.imagePath();
            case "L":
                return LACLOUD.imagePath();
            case "W":
                return W_WATER.imagePath();
            default:
                return MASSAGE_CHAIR.imagePath();
        }
    }
    
}
