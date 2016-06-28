package com.tadpole.poem.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by jerryjiang on 15/6/2016.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable{

    private Long id;
    private String name;
    private Integer age;
    private Integer birth;
    private Integer death;
    private String[] desc;
    private String period;
    private String avatar;
    private String zi;
    private String hao;

}
