package tinkoff;

import ru.tinkoff.piapi.core.InvestApi;
//создание токена
public class CreateToken {
    
    public static InvestApi getToken(){
        var api = InvestApi.create(DataStorage.token);
        return api;
    }
}