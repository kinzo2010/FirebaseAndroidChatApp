package kiki.chat.firebase.com.firebasechat.ui.search;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.ActivitySearchBinding;
import kiki.chat.firebase.com.firebasechat.ui.BaseActivity;
import kiki.chat.firebase.com.firebasechat.util.SearchType;

public class SearchActivity extends BaseActivity {

    /**
     *
     * 1 - Tìm kiếm thông tin cá nhân
     * 2 - Tìm kiếm để thêm tin nhắn mới
     *
     */

    public static final String SEARCH_TYPE_EXTRA_KEY = "search-type-extra-key";


    ActivitySearchBinding activitySearchBinding;
    SearchViewModel searchViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        switch (getIntent().getIntExtra(SEARCH_TYPE_EXTRA_KEY, 1)) {

            case 1 :{

                searchViewModel = new SearchViewModel(this, this, SearchType.PROFILE, activitySearchBinding);
                activitySearchBinding.setViewModel(searchViewModel);
                break;
            }

            case 2 : {

                searchViewModel = new SearchViewModel(this, this, SearchType.FOR_SEND_MESSAGE, activitySearchBinding);
                activitySearchBinding.setViewModel(searchViewModel);

                break;
            }


        }



    }
}
