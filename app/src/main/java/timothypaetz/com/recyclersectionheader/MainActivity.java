package timothypaetz.com.recyclersectionheader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                                                              LinearLayoutManager.VERTICAL,
                                                              false));

        final List<Person> people = getPeople();

        RecyclerSectionItemDecorationStyle sectionItemDecoration =
            new RecyclerSectionItemDecorationStyle(getResources().getDimensionPixelSize(R.dimen.recycler_section_header_height),
                                              true,
                                              getSectionCallback(people));
        recyclerView.addItemDecoration(sectionItemDecoration);

        recyclerView.setAdapter(new PersonAdapter(getLayoutInflater(),
                                                  people,
                                                  R.layout.recycler_row));
    }

    private RecyclerSectionItemDecorationStyle.SectionCallback getSectionCallback(final List<Person> people) {
    return new RecyclerSectionItemDecorationStyle.SectionCallback() {
        @Override
            public boolean isSection(int position) {
                return position == 0
                    || people.get(position)
                             .getLastName()
                             .charAt(0) != people.get(position - 1)
                                                 .getLastName()
                                                 .charAt(0);
            }

            @Override
            public CharSequence getSectionType(int position) {
                return people.get(position)
                        .getLastName().subSequence(0,
                                1);
            }

            @Override
            public CharSequence getSectionHeader(int position) {
                return people.get(position)
                             .getLastName()
                             .subSequence(0,
                                          1);
            }
    };
    }


    private List<Person> getPeople() {
        PersonRepo personRepo = new PersonRepo();
        List<Person> people = personRepo.getPeople();
        Collections.sort(people);
        return people;
    }

//    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<Person> people) {
//        return new RecyclerSectionItemDecoration.SectionCallback() {
//            @Override
//            public boolean isSection(int position) {
//                return position == 0
//                    || people.get(position)
//                             .getLastName()
//                             .charAt(0) != people.get(position - 1)
//                                                 .getLastName()
//                                                 .charAt(0);
//            }
//
//            @Override
//            public CharSequence getSectionType(int position) {
//                return people.get(position)
//                        .getLastName().subSequence(0,
//                                1);
//            }
//
//            @Override
//            public CharSequence getSectionHeader(int position) {
//                return people.get(position)
//                             .getLastName()
//                             .subSequence(0,
//                                          1);
//            }
//
//        };
//    }


}
