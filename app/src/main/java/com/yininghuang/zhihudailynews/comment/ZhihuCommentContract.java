package com.yininghuang.zhihudailynews.comment;

import com.yininghuang.zhihudailynews.BasePresenter;
import com.yininghuang.zhihudailynews.BaseView;
import com.yininghuang.zhihudailynews.model.ZhihuComments;

import java.util.List;

/**
 * Created by Yining Huang on 2016/10/20.
 */

public class ZhihuCommentContract {

    public interface View extends BaseView<Presenter> {

        void showComments(List<ZhihuComments.ZhihuComment> comments);

        void showCommentsCount(int count);

        void addHistoryComment(List<ZhihuComments.ZhihuComment> comments);

        void setLoadingStatus(boolean status);

        void showLoadError();

        void showLoadComplete();
    }

    public interface Presenter extends BasePresenter {

        void init(int newsId);

        void reload();

        void queryHistoryComments(int userId);
    }
}
