package br.net.easify.marvelapitest.Model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTimeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


import br.net.easify.marvelapitest.interfaces.ICharacterDelegate;

public class DataFactory {

    private static DataFactory instance = null;
    private Context context;
    private DatabaseHandler db;
    private int characterListCount = 0;
    private int characterListTotal = 0;
    private int characterListOffset = 0;
    private int characterListLimit = 0;
    public List<Character> characterList = new ArrayList<>();

    private DataFactory() {

    }

    public static DataFactory sharedInstance() {

        if (instance == null) {
            instance = new DataFactory();
        }

        return instance;
    }

    public void setContext(Context context) {

        this.context = context;
        this.db = new DatabaseHandler(this.context);
        this.loadLocalDatabaseData();
    }

    private void loadLocalDatabaseData() {

        Total total = this.db.getData();

        this.characterListCount = total.getCount();
        this.characterListTotal = total.getTotal();
        this.characterListOffset = total.getOffset();
        this.characterListLimit = total.getLimit();

        this.characterList = this.db.getCharacterList();
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public List<Character> getCharacterList() {

        return this.characterList;
    }

    public void getCharacters(ICharacterDelegate delegate) {

        if ( this.characterList.size() > 0 ) {
            delegate.onGetCharacters(true);
            return;
        }

        new CharactersAsync(delegate).execute();
    }

    public Character getCharacter(int position) {

        return characterList.get(position);
    }

    public void getMoreCharacters(ICharacterDelegate delegate) {

        if ( characterListOffset >= characterListTotal ) {
            delegate.onGetCharacters(true);
            return;
        }

        characterListOffset += characterListLimit;
        new CharactersAsync(delegate).execute();
    }

    public class CharactersAsync extends AsyncTask<Void, Void, String> {

        private ICharacterDelegate delegate;

        public CharactersAsync(ICharacterDelegate delegate) {
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (!isNetworkAvailable()) {
                return "";
            }

            String jsonStr = "";

            try {
                Long currentTime = DateTimeUtils.currentTimeMillis();
                byte[] hash = DigestUtils.md5(currentTime + Constants.PRIVATE_KEY + Constants.PUBLIC_KEY);
                String hashStr = new String(Hex.encodeHex(hash));

                String urlStr =
                        Constants.MARVEL_BASE_URL +
                                "characters?ts=" +
                                currentTime +
                                "&apikey=" +
                                Constants.PUBLIC_KEY +
                                "&hash=" +
                                hashStr +
                                "&offset=" +
                                characterListOffset +
                                "&orderBy=name";

                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(30000);
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");

                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("Accept-Charset", "UTF-8");

                connection.setUseCaches(false);

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    jsonStr += line;
                    line = reader.readLine();
                }
                reader.close();
                inputStreamReader.close();
                inputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);

            if (jsonStr.length() > 0) {
                try {
                    JSONObject obj = new JSONObject(URLDecoder.decode(jsonStr, "UTF-8"));
                    JSONObject data = obj.getJSONObject("data");

                    characterListCount = Integer.parseInt(data.getString("count"));
                    characterListTotal = Integer.parseInt(data.getString("total"));
                    characterListLimit = Integer.parseInt(data.getString("limit"));
                    characterListOffset = Integer.parseInt(data.getString("offset"));

                    db.setData(characterListCount, characterListTotal, characterListLimit, characterListOffset);

                    JSONArray results = data.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject hero = results.getJSONObject(i);
                        JSONObject thumbnail = hero.getJSONObject("thumbnail");
                        String thumbUrl = thumbnail.getString("path") + "." + thumbnail.getString("extension");

                        Character character = new Character(
                                Integer.parseInt(hero.getString("id")),
                                hero.getString("name"),
                                hero.getString("description"),
                                thumbUrl);

                        db.addCharacter(character);
                        characterList.add(character);
                    }

                    delegate.onGetCharacters(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    delegate.onGetCharacters(false);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    delegate.onGetCharacters(false);
                }
            } else {
                delegate.onGetCharacters(false);
            }
        }
    }
}
