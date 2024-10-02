package tinkoff;

import java.util.List;

//хранение переменных
public class DataStorage {
        

    public static final String token = "";
    
    public static List<String> figiList = List.of(
        "BBG004730ZJ9", "BBG000000001", "BBG006L8G4H1",
        "TCS00A105EX7", "BBG0100R9963", "BBG004731032",
        "TCS00A106YF0", "BBG000QJW156"
    );

    public static final String db_host = "localhost";
    public static final String db_port = "5432";
    public static final String db_name = "JavaTradingBotTinkoff";
    public static final String db_user= "postgres";
    public static final String db_password = "1";
    public static final String db_ssl = "False";
    
}