package com.bmstu.rsoi_lab3.models;

import com.bmstu.rsoi_lab3.markers.SailorBackend;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Created by Александр on 18.02.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SailorsPage implements SailorBackend {
    private List<SailorsPreview> content;
    private int totalElements;
    private boolean last;
    private int totalPages;
    private int size;
    private int number;
    private boolean first;
    private int numberOfElements;

    public SailorsPage(List<SailorsPreview> content, boolean first, boolean last, int number, int numberOfElements, int size, int totalElements, int totalPages) {
        this.content = content;
        this.first = first;
        this.last = last;
        this.number = number;
        this.numberOfElements = numberOfElements;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public SailorsPage() {
    }

    public List<SailorsPreview> getContent() {
        return content;
    }

    public void setContent(List<SailorsPreview> content) {
        this.content = content;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "SailorsPage{" +
                "content=" + content +
                ", totalElements=" + totalElements +
                ", last=" + last +
                ", totalPages=" + totalPages +
                ", size=" + size +
                ", number=" + number +
                ", first=" + first +
                ", numberOfElements=" + numberOfElements +
                '}';
    }
}
