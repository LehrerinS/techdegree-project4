package com.teamtreehouse.blog;

import com.teamtreehouse.blog.dao.BlogDao;
import com.teamtreehouse.blog.dao.SimpleBlogDao;
import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.CommentEntry;
import org.antlr.v4.runtime.atn.SemanticContext;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.route.HttpMethod.get;

public class Main {

    public static void main(String[] args){

        BlogDao dao = new SimpleBlogDao();
        BlogEntry blog1 = new BlogEntry("The Elephant Rope (Belief)",  new Date(), "A gentleman was walking through an elephant camp, and he spotted that the elephants weren’t being kept in cages or held by the use of chains. All that was holding them back from escaping the camp, was a small piece of rope tied to one of their legs. As the man gazed upon the elephants, he was completely confused as to why the elephants didn’t just use their strength to break the rope and escape the camp. They could easily have done so, but instead they didn’t try to at all. Curious and wanting to know the answer, he asked a trainer nearby why the elephants were just standing there and never tried to escape. The trainer replied: \"when they are very young and much smaller we use the same size rope to tie them and, at that age, it’s enough to hold them. As they grow up, they are conditioned to believe they cannot break away. They believe the rope can still hold them, so they never try to break free\" The only reason that the elephants weren’t breaking free and escaping from the camp was because over time they adopted the belief that it just wasn’t possible.");
        BlogEntry blog2 = new BlogEntry("Thinking Out of the Box (Creative Thinking)", new Date(), "In a small Italian town, hundreds of years ago, a small business owner owed a large sum of money to a loan-shark. The loan-shark was a very old, unattractive looking guy that just so happened to fancy the business owner’s daughter. He decided to offer the businessman a deal that would completely wipe out the debt he owed him. However, the catch was that we would only wipe out the debt if he could marry the businessman’s daughter. Needless to say, this proposal was met with a look of disgust. The loan-shark said that he would place two pebbles into a bag, one white and one black. The daughter would then have to reach into the bag and pick out a pebble. If it was black, the debt would be wiped, but the loan-shark would then marry her. If it was white, the debt would also be wiped, but the daughter wouldn’t have to marry the loan-shark. Standing on a pebble strewn path in the businessman’s garden, the loan-shark bent over and picked up two pebbles. Whilst he was picking them up, the daughter noticed that he’d picked up two black pebbles and placed them both into the bag. He then asked the daughter to reach into the bag and pick one. The daughter naturally had three choices as to what she could have done: Refuse to pick a pebble from the bag. Take both pebbles out of the bag and expose the loan-shark for cheating. Pick a pebble from the bag fully well knowing it was black and sacrifice herself for her father’s freedom. She drew out a pebble from the bag, and before looking at it ‘accidentally’ dropped it into the midst of the other pebbles. She said to the loan-shark: “Oh, how clumsy of me. Never mind, if you look into the bag for the one that is left, you will be able to tell which pebble I picked. The pebble left in the bag is obviously black, and seeing as the loan-shark didn’t want to be exposed, he had to play along as if the pebble the daughter dropped was white, and clear her father’s debt.");
        BlogEntry blog3 = new BlogEntry("The Group of Frogs (Encouragement)", new Date(), "As a group of frogs were traveling through the woods, two of them fell into a deep pit. When the other frogs crowded around the pit and saw how deep it was, they told the two frogs that they’re was no hope left for them. However, the two frogs decided to ignore what the others were saying and they proceeded to try and jump out of the pit. Despite their efforts, the group of frogs at the top of he pit were still saying that they should just give up. That they would never make it out. Eventually, one of the frogs took heed to what the others were saying and he gave up, falling down to his death. The other frog continued to jump as hard as he could. Again, the crowd of frogs yelled at him to stop the pain and just die. He jumped even harder and finally made it out. When he got out, the other frogs said, “Did you not hear us?” The frog explained to them that he was deaf. He thought they were encouraging him the entire time.");
        dao.addEntry(blog1);
        dao.addEntry(blog2);
        dao.addEntry(blog3);

        staticFileLocation("/public");

        before((req, res) -> {
            if (req.cookie("password") != null) {
                req.attribute("password", req.cookie("password"));
            }
        });

        before("/blogs/new", (req, res)->{
            if(req.attribute("password") == null){
                res.redirect("/blogs/password");
                halt();
            }
        });


        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("blogs", dao.findAllEntries());
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/blogs/new", (req, res) -> {
            return new ModelAndView(null, "new.hbs");
        }, new HandlebarsTemplateEngine());

        post("/", (req, res) -> {
            String title = req.queryParams("title");
            String body = req.queryParams("entry");
            if(!title.equals("")) {
                BlogEntry newEntry = new BlogEntry(title, new Date(), body);
                dao.addEntry(newEntry);
            }
            res.redirect("/");
            return null;
        });

        get("/blogs/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            String providedPassword = req.cookie("password");
            model.put("password", providedPassword);
            return new ModelAndView(model, "password.hbs");
        }, new HandlebarsTemplateEngine());

        post("/blogs/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            String userGivenPassword = req.queryParams("password");
            if (userGivenPassword.equals("admin")) {
                res.cookie("password", userGivenPassword);
                model.put("password", userGivenPassword);
            }
//            TODO: sanja add flash message if wrong pass is given, in order to try it again
            res.redirect("/blogs/password");
            return null;
//            return new ModelAndView(model, "password.hbs");
        });

        before("/blogs/:slug/edit", (req, res)->{
            if(req.attribute("password") == null){
                res.redirect("/blogs/password");
                halt();
            }
        });

        get("/blogs/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("blog", dao.findEntryBySlug(req.params("slug")));
            return new ModelAndView(model, "detail.hbs");
        }, new HandlebarsTemplateEngine());

        get("/blogs/:slug/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("blog", dao.findEntryBySlug(req.params("slug")));
            return new ModelAndView(model, "edit.hbs");
        }, new HandlebarsTemplateEngine());

        post("/blogs/:slug/tag", (req, res) -> {
            BlogEntry foundEntry = dao.findEntryBySlug(req.params("slug"));
            boolean added = foundEntry.addTags(req.queryParams("tag"));
            res.redirect("/");
            return null;
        });

        post("/blogs/:slug", (req, res) -> {
            BlogEntry foundEntry = dao.findEntryBySlug(req.params("slug"));
            List<CommentEntry> foundComments = foundEntry.getComments();
            if (req.queryParams("title")!=null || req.queryParams("entry")!=null) {
                dao.removeEntry(foundEntry);
                String title = req.queryParams("title");
                String body = req.queryParams("entry");
                BlogEntry newEntry = new BlogEntry(title, new Date(), body, foundComments);
                dao.addEntry(newEntry);
                res.redirect("/");
            }
            else if (req.queryParams("name")!=null || req.queryParams("comment")!=null){
                String name = req.queryParams("name");
                String comment = req.queryParams("comment");
                CommentEntry newComment = new CommentEntry(name, new Date(), comment);
                foundComments.add(newComment);
            res.redirect("/");
            }
            return null;
        });

        post("/blogs/:slug/delete", (req, res) -> {
            BlogEntry foundEntry = dao.findEntryBySlug(req.params("slug"));
            dao.removeEntry(foundEntry);
            res.redirect("/");
            return null;
        });

    }

}
