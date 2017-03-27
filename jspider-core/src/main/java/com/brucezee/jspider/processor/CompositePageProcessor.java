package com.brucezee.jspider.processor;

import com.brucezee.jspider.Page;
import com.brucezee.jspider.Result;
import com.brucezee.jspider.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 混合页面解析器
 * Created by brucezee on 2017/1/13.
 */
public class CompositePageProcessor implements PageProcessor {
    private List<SubPageProcessor> processors;
    public CompositePageProcessor(List<SubPageProcessor> processors) {
        this.processors = processors;
    }
    public CompositePageProcessor(SubPageProcessor... processors) {
        this(Arrays.asList(processors));
    }
    public CompositePageProcessor() {
        this(new ArrayList<SubPageProcessor>());
    }

    @Override
    public Result process(Request request, Page page) {
        for (SubPageProcessor processor : processors) {
            if (processor.isMatch(request)) {
                return processor.process(request, page);
            }
        }
        throw new IllegalArgumentException("No sub page processor can process request " + request);
    }

    public CompositePageProcessor addSubPageProcessor(SubPageProcessor pageProcessor) {
        this.processors.add(pageProcessor);
        return this;
    }
}
