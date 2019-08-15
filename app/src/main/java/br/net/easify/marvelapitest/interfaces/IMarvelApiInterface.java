package br.net.easify.marvelapitest.interfaces;

import br.net.easify.marvelapitest.Model.CharacterResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface IMarvelApiInterface {

    @Headers({"accept: application/json", "Accept-Charset: UTF-8"})
    @GET("characters")
    Call<CharacterResponse> getCharacters(@Query("ts") Long ts, @Query("apikey") String apikey, @Query("hash") String hash, @Query("offset") int offset);
}
