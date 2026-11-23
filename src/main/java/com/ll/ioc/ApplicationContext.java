package com.ll.ioc;

import com.ll.domain.testPost.testPost.repository.TestPostRepository;
import com.ll.domain.testPost.testPost.service.TestFacadePostService;
import com.ll.domain.testPost.testPost.service.TestPostService;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    // 빈 이름 -> 이미 만들어진 빈 (싱글톤 보관소)
    private final Map<String, Object> beans = new HashMap<>();

    public ApplicationContext() {

    }

    @SuppressWarnings("unchecked")
    public <T> T genBean(String beanName) {
        Object bean = beans.get(beanName);

        if (bean == null) {
            bean = createBean(beanName);
            beans.put(beanName, bean);
        }

        return (T) bean;
    }

    // v1: 빈 생성 방법을 하드코딩한다. 의존 빈은 genBean 으로 얻어 같은 객체를 주입한다.
    private Object createBean(String beanName) {
        return switch (beanName) {
            case "testPostRepository" -> new TestPostRepository();
            case "testPostService" -> new TestPostService(
                    genBean("testPostRepository")
            );
            case "testFacadePostService" -> new TestFacadePostService(
                    genBean("testPostService"),
                    genBean("testPostRepository")
            );
            default -> throw new IllegalArgumentException("등록되지 않은 빈입니다: " + beanName);
        };
    }
}
