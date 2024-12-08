package aoc.dcw;

import aoc.dcw.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * General solution is to create a graph of page nodes that link to the nodes that must come before it. Then sort
 * the page lists by searching the graph to see if one page must come before another.
 */
public class Day5 extends AoCDay {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    List<String> lines;
    List<List<Integer>> updatePages;
    Map<Integer, PageGraphNode> pageGraphNodes = new HashMap<>();

    public void run() {

        lines = Utilities.getLines("day5.txt");
        int divide = lines.indexOf("\r");

        // Create the graph of page orders
        lines.subList(0, divide).stream().map(String::trim).map(s -> List.of(s.trim().split("\\|"))).map(l -> l.stream().map(Integer::valueOf).toList()).forEach(this::parseRule);

        updatePages = lines.subList(divide + 1, lines.size()).stream().map(String::trim).map(s -> List.of(s.split(","))).map(l -> l.stream().map(Integer::valueOf).toList()).toList();

        logger.info("done parsing rules");

        int unorderedMiddleSum = 0;
        int orderedMiddleSum = 0;

        // Sort all page lists and sum based on whether the list was already sorted or not
        for (List<Integer> pages : updatePages) {
            List<Integer> sorted = new ArrayList<>(pages);
            // Comparator just checks to see if one page is before the other in the graph, limiting the scope to the current page list
            sorted.sort((i0, i1) -> Objects.equals(i0, i1) ? 0 : pageGraphNodes.get(i0).isBefore(i1, pages) ? -1 : 1);
            int middle = sorted.get(sorted.size() / 2);
            if (pages.equals(sorted)) orderedMiddleSum += middle;
            else unorderedMiddleSum += middle;
        }

        logger.info("unorderedMiddleSum: {}", unorderedMiddleSum);
        logger.info("orderedMiddleSum: {}", orderedMiddleSum);
    }

    public void parseRule(List<Integer> rule) {
        logger.info("parsing rule: {}", rule);
        PageGraphNode beforeNode = pageGraphNodes.computeIfAbsent(rule.get(0), PageGraphNode::new);
        PageGraphNode afterNode = pageGraphNodes.computeIfAbsent(rule.get(1), PageGraphNode::new);
        beforeNode.after.add(afterNode);
    }

    // Represents a single page and all the pages that must be after it
    public static class PageGraphNode {
        final Integer value;
        final Set<PageGraphNode> after = new HashSet<>(); // These nodes must come after this node

        // Don't need to track the "after" nodes since we're assuming there are no invalid rules
        public PageGraphNode(Integer value) {
            this.value = value;
        }

        // returns true if this node must come before the node at i
        public boolean isBefore(int i, List<Integer> includeOnly) {
            return after.stream().filter(g -> includeOnly.contains(g.value)).anyMatch(b -> b.value == i || b.isBefore(i, includeOnly));
        }
    }
}