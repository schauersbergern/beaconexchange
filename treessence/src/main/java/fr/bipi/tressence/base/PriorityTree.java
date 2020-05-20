/*
 * Copyright (C) 2017 bastien
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
package fr.bipi.tressence.base;

import org.jetbrains.annotations.NotNull;

import fr.bipi.tressence.common.filters.Filter;
import fr.bipi.tressence.common.filters.NoFilter;
import fr.bipi.tressence.common.filters.PriorityFilter;
import timber.log.Timber;

/**
 * Base class to filter logs by priority
 */
public class PriorityTree extends Timber.DebugTree {

    private final PriorityFilter priorityFilter;
    private Filter filter = NoFilter.INSTANCE;

    /**
     * @param priority priority from witch log will be logged
     */
    public PriorityTree(int priority) {
        this.priorityFilter = new PriorityFilter(priority);
    }

    /**
     * Add additional {@link Filter}
     *
     * @param f Filter
     * @return itself
     */
    public PriorityTree withFilter(@NotNull Filter f) {
        this.filter = f;
        return this;
    }

    @Override
    protected boolean isLoggable(int priority) {
        return isLoggable("", priority);
    }

    @Override
    public boolean isLoggable(String tag, int priority) {
        return priorityFilter.isLoggable(priority, tag) && filter.isLoggable(priority, tag);
    }

    public PriorityFilter getPriorityFilter() {
        return priorityFilter;
    }

    public Filter getFilter() {
        return filter;
    }

    /**
     * Use the additional filter to determine if this log needs to be skipped
     *
     * @param priority Log priority
     * @param tag      Log tag
     * @param message  Log message
     * @param t        Log throwable
     * @return true if needed to be skipped or false
     */
    protected boolean skipLog(int priority, String tag, @NotNull String message, Throwable t) {
        return filter.skipLog(priority, tag, message, t);
    }
}
