package com.tadpole.poem.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jerryjiang on 16/6/2016.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poem {
    private Long id;
    private String title;
    private String[] content;
    private Long authorId;
    private String pinyin;
}
