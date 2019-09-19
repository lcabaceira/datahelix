/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeGranularity;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.time.*;
import java.util.Iterator;

class RandomDateGenerator {
    private final OffsetDateTime minDate;
    private final OffsetDateTime maxDate;
    private final DateTimeGranularity granularity;

    RandomDateGenerator(OffsetDateTime minDate, OffsetDateTime maxDate, DateTimeGranularity granularity) {
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.granularity = granularity;
    }

    public OffsetDateTime next(RandomNumberGenerator random) {
        long min = getMilli(minDate);
        long max = getMilli(maxDate) - 1;

        long generatedLong = (long) random.nextDouble(min, max);

        OffsetDateTime generatedDate = Instant.ofEpochMilli(generatedLong).atZone(ZoneOffset.UTC).toOffsetDateTime();

        return granularity.trimToGranularity(generatedDate);
    }

    private long getMilli(OffsetDateTime date) {
        return date.toInstant().toEpochMilli();
    }
}
