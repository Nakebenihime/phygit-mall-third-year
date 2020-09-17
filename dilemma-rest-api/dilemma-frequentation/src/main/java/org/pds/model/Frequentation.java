package org.pds.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "frequentationindex", type = "frequentation")
@AllArgsConstructor
@NoArgsConstructor
public class Frequentation {

    @Id
    private String id;
    private String id_client;
    private String storeName;
    private String in_out;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy/MM/dd HH:mm:ss")
    private String date;

    @Override
    public String toString() {
        return "Frequentation{" +
                "id='" + id + '\'' +
                ", id_client='" + id_client + '\'' +
                ", storeName='" + storeName + '\'' +
                ", in_out='" + in_out + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}