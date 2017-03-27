package com.brucezee.jspider.pipeline;

import com.brucezee.jspider.Result;
import com.brucezee.jspider.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 混合结果集处理器
 * Created by brucezee on 2017/1/16.
 */
public class CompositePipeline implements Pipeline {
    private List<SubPipeline> pipelines;
    public CompositePipeline(List<SubPipeline> pipelines) {
        this.pipelines = pipelines;
    }
    public CompositePipeline(SubPipeline... pipelines) {
        this(Arrays.asList(pipelines));
    }
    public CompositePipeline() {
        this(new ArrayList<SubPipeline>());
    }

    @Override
    public void persist(Request request, Result result) {
        for (SubPipeline pipeline : pipelines) {
            if (pipeline.isMatch(request)) {
                pipeline.persist(request, result);
            }
        }
    }

    public CompositePipeline addSubPipeline(SubPipeline pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }
}
