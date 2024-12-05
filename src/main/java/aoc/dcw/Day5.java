package aoc.dcw;

import aoc.AoC;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day5 {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    List<String> lines;
    List<List<Integer>> updatePages;
    Map<Integer,GraphNode> graph = new HashMap<>();

    public void run() throws IOException {

        lines = List.of(IOUtils.toString(AoC.class.getClassLoader().getResourceAsStream("day5.txt"), Charset.defaultCharset()).split("\n"));
        int divide = lines.indexOf("\r");

        // Create the graph of page orders
        lines.subList(0,divide)
                .stream()
                .map(String::trim)
                .map(s -> List.of(s.trim().split("\\|")))
                .map(l -> l.stream().map(Integer::valueOf).toList())
                .forEach(this::parseRule);

        updatePages = lines.subList(divide+1,lines.size())
                .stream()
                .map(String::trim)
                .map(s -> List.of(s.split(",")))
                .map(l -> l.stream().map(Integer::valueOf).toList()).toList();

        logger.info("done parsing rules");

        int unorderedMiddleSum = 0;
        int orderedMiddleSum = 0;

        // Sort all page lists and sum based on whether the list was already sorted or not
        for(List<Integer> pages : updatePages) {
            List<Integer> sorted = new ArrayList<>(pages);
            // Comparator just checks to see if one page is before the other in the graph, limiting the scope to the current page list
            sorted.sort((i0,i1) -> Objects.equals(i0, i1) ? 0 : graph.get(i0).isAfter(i1, pages) ? -1 : 1);
            int middle = sorted.get(sorted.size()/2);
            if(pages.equals(sorted)) orderedMiddleSum += middle;
            else unorderedMiddleSum += middle;
        }

        logger.info("unorderedMiddleSum: {}",unorderedMiddleSum);
        logger.info("orderedMiddleSum: {}", orderedMiddleSum);
    }

    public void parseRule(List<Integer> rule) {
        logger.info("parsing rule: {}",rule);
        GraphNode beforeNode = graph.computeIfAbsent(rule.get(0),GraphNode::new);
        GraphNode afterNode = graph.computeIfAbsent(rule.get(1),GraphNode::new);
        beforeNode.before.add(afterNode);
    }

    // Represents a single page and all the pages that it must come before
    public static class GraphNode {
        final Integer value;
        final Set<GraphNode> before = new HashSet<>(); // This node must come before these other nodes
        // Don't need to track the "after" nodes since we're assuming there are no invalid rules
        public GraphNode(Integer value) {
            this.value = value;
        }
        // returns true if this node must come after the page at i
        public boolean isAfter(int i, List<Integer> includeOnly){
            return before.stream().filter(g -> includeOnly.contains(g.value)).anyMatch(b -> b.value == i || b.isAfter(i, includeOnly));
        }
    }
}