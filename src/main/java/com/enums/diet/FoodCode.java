package com.enums.diet;

public enum FoodCode {
    현미밥("1"),
    닭가슴살("2"),
    계란("3"),
    찐고구마("4"),
    감자("5"),
    아몬드("6"),
    두유("7"),
    플레인요거트("8"),
    적색파프리카("9"),
    주황색파프리카("10"),
    황색파프리카("11"),
    녹색파프리카("12"),
    양상추("13"),
    오이("14"),
    바나나("15"),
    토마토("16");

    private final String code;
    FoodCode(String code) {this.code = code;}
    public String getId() {return code;}
}
