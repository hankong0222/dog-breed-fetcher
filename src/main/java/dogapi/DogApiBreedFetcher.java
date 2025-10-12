package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException{
        // return statement included so that the starter code can compile and run.
        final String url = "https://dog.ceo/api/breed/" + breed.toLowerCase() + "/list";
        Request request = new Request.Builder().url(url).build();
        ArrayList<String> list = new ArrayList<>();

        try {
            final Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                final JSONObject responsebody = new JSONObject(response.body().string());
                if (responsebody.get("status").toString().equals("success")) {
                    JSONArray m = responsebody.getJSONArray("message");
                    for (int i = 0; i < m.length(); i++) {
                        list.add(m.get(i).toString());
                    }
                }
            } else  {
                throw new BreedNotFoundException("Unexpected breed " + breed);
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
        return list;
    }
}