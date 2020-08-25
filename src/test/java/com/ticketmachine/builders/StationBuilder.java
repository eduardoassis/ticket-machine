package com.ticketmachine.builders;

import com.ticketmachine.models.Station;

public class StationBuilder {

    private Station station;

    public StationBuilder() {
        this.station = new Station();
    }

    public StationBuilder withName(final String name) {
        this.station.setName(name);
        return this;
    }

    public Station build() {
        return this.station;
    }
}