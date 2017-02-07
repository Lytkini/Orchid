package com.eden.orchid.impl.generators;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.resources.OrchidPage;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public class PostsGenerator implements Generator {

    private List<OrchidPage> posts;

    @Override
    public int priority() {
        return 700;
    }

    @Override
    public String getName() {
        return "posts";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JSONElement startIndexing() {
        JSONObject sitePosts = new JSONObject();

        List<OrchidResource> resources = Orchid.getResources().getResourceDirEntries("posts", null, true);
        posts = new ArrayList<>();

        for (OrchidResource entry : resources) {
            if(!EdenUtils.isEmpty(entry.queryEmbeddedData("title"))) {
                entry.getReference().setTitle(entry.queryEmbeddedData("title").toString());
            }
            else {
                entry.getReference().setTitle(entry.getReference().getFileName());
            }

            if(entry.queryEmbeddedData("root") != null) {
                if(Boolean.parseBoolean(entry.queryEmbeddedData("root").toString())) {
                    entry.getReference().stripBasePath("posts/");
                }
            }

            entry.getReference().setUsePrettyUrl(true);

            OrchidPage post = new OrchidPage(entry);

            posts.add(post);

            JSONObject index = new JSONObject();
            index.put("name", post.getReference().getTitle());
            index.put("title", post.getReference().getTitle());
            index.put("url", post.getReference().toString());

            OrchidUtils.buildTaxonomy(entry, sitePosts, index);
        }

        return new JSONElement(sitePosts);
    }

    @Override
    public void startGeneration() {
        int i = 0;
        for (OrchidPage post : posts) {
            if (next(i) != null) { post.setNext(next(i)); }
            if (previous(i) != null) { post.setPrevious(previous(i)); }
            post.renderTemplate("templates/pages/page.twig");
            i++;
        }
    }

    public OrchidPage previous(int i) {
        if (posts.size() > 1) {
            if (i == 0) {
                return posts.get(posts.size() - 1);
            }
            else {
                return posts.get(i - 1);
            }
        }

        return null;
    }

    public OrchidPage next(int i) {
        if (posts.size() > 1) {
            if (i == posts.size() - 1) {
                return posts.get(0);
            }
            else {
                return posts.get(i + 1);
            }
        }

        return null;
    }
}