/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.combobox.test;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.combobox.demo.ComboBoxView;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.demo.TabbedComponentDemoTest;

import elemental.json.JsonObject;

/**
 * Integration tests for the {@link ComboBoxView}.
 */
public class ComboBoxIT extends TabbedComponentDemoTest {

    @Test
    public void openStringBoxAndSelectAnItem() {
        openTabAndCheckForErrors("");
        ComboBoxElement comboBox = $(ComboBoxElement.class)
                .id("string-selection-box");
        WebElement message = layout
                .findElement(By.id("string-selection-message"));

        comboBox.openPopup();
        executeScript(
                "arguments[0].selectedItem = arguments[0].filteredItems[2]",
                comboBox);

        Assert.assertEquals("Selected browser: Opera", message.getText());
    }

    @Test
    public void openObjectBoxAndSelectAnItem() {
        openTabAndCheckForErrors("");
        ComboBoxElement comboBox = $(ComboBoxElement.class)
                .id("object-selection-box");
        WebElement message = layout
                .findElement(By.id("object-selection-message"));

        comboBox.openPopup();
        executeScript(
                "arguments[0].selectedItem = arguments[0].filteredItems[1]",
                comboBox);

        waitUntil(driver -> message.getText().equals(
                "Selected song: Sculpted\nFrom album: Two Fold Pt.1\nBy artist: Haywyre"));
    }

    @Test
    public void setEnabledCombobox() {
        openTabAndCheckForErrors("");
        ComboBoxElement comboBox = $(ComboBoxElement.class)
                .id("disabled-combo-box");
        WebElement message = layout
                .findElement(By.id("value-selection-message"));
        Assert.assertEquals("", message.getText());

        executeScript("arguments[0].removeAttribute(\"disabled\");"
                + "arguments[0].selectedItem = arguments[0].filteredItems[1]",
                comboBox);
        message = layout.findElement(By.id("value-selection-message"));
        Assert.assertEquals("", message.getText());
    }

    @Test
    public void openValueBoxSelectTwoItems() {
        openTabAndCheckForErrors("");
        ComboBoxElement comboBox = $(ComboBoxElement.class)
                .id("value-selection-box");
        WebElement message = layout
                .findElement(By.id("value-selection-message"));

        comboBox.openPopup();
        executeScript(
                "arguments[0].selectedItem = arguments[0].filteredItems[1]",
                comboBox);

        waitUntil(
                driver -> message.getText().equals("Selected artist: Haywyre"));

        executeScript(
                "arguments[0].selectedItem = arguments[0].filteredItems[0]",
                comboBox);

        waitUntil(driver -> message.getText().equals(
                "Selected artist: Haircuts for Men\nThe old selection was: Haywyre"));
    }

    @Test
    public void openTemplateBox() {
        openTabAndCheckForErrors("using-templates");

        ComboBoxElement comboBox = $(ComboBoxElement.class)
                .id("template-selection-box");
        WebElement message = layout
                .findElement(By.id("template-selection-message"));

        comboBox.openPopup();
        List<Map<String, ?>> items = (List<Map<String, ?>>) executeScript(
                "return arguments[0].filteredItems", comboBox);

        items.forEach(item -> {
            Assert.assertNotNull(item.get("key"));
            Assert.assertNotNull(item.get("label"));
            Assert.assertNotNull(item.get("song"));
            Assert.assertNotNull(item.get("artist"));
        });

        Map<String, ?> firstItem = items.get(0);
        Assert.assertEquals("A V Club Disagrees", firstItem.get("label"));
        Assert.assertEquals("A V Club Disagrees", firstItem.get("song"));
        Assert.assertEquals("Haircuts for Men", firstItem.get("artist"));

        executeScript(
                "arguments[0].selectedItem = arguments[0].filteredItems[1]",
                comboBox);

        waitUntil(
                driver -> message.getText().equals("Selected artist: Haywyre"));

        executeScript(
                "arguments[0].selectedItem = arguments[0].filteredItems[0]",
                comboBox);

        waitUntil(driver -> message.getText().equals(
                "Selected artist: Haircuts for Men\nThe old selection was: Haywyre"));
    }

    @Test
    public void templateBoxCustomFiltering_filterableByArtist() {
        openTabAndCheckForErrors("using-templates");
        ComboBoxElement comboBox = $(ComboBoxElement.class)
                .id("template-selection-box");
        comboBox.openPopup();
        comboBox.setFilter("ha");

        waitUntil(driver -> ((List<Map<String, ?>>) executeScript(
                "return arguments[0].filteredItems", comboBox)).size() == 2);

        List<Map<String, ?>> items = (List<Map<String, ?>>) executeScript(
                "return arguments[0].filteredItems", comboBox);

        Assert.assertEquals("Haircuts for Men", items.get(0).get("artist"));
        Assert.assertEquals("Haywyre", items.get(1).get("artist"));
    }

    @Test
    public void componentBoxCustomFiltering_filterableByArtist() {
        openTabAndCheckForErrors("using-components");
        ComboBoxElement comboBox = $(ComboBoxElement.class)
                .id("component-selection-box");
        comboBox.openPopup();
        comboBox.setFilter("ha");

        waitUntil(driver -> ((List<Map<String, ?>>) executeScript(
                "return arguments[0].filteredItems", comboBox)).size() == 2);

        List<Map<String, ?>> items = (List<Map<String, ?>>) executeScript(
                "return arguments[0].filteredItems", comboBox);

        Assert.assertEquals("A V Club Disagrees", items.get(0).get("label"));
        Assert.assertEquals("Sculpted", items.get(1).get("label"));
    }

    @Test
    public void openComponentBox() {
        openTabAndCheckForErrors("using-components");

        ComboBoxElement comboBox = $(ComboBoxElement.class)
                .id("component-selection-box");
        WebElement message = layout
                .findElement(By.id("component-selection-message"));

        comboBox.openPopup();
        List<Map<String, ?>> items = (List<Map<String, ?>>) executeScript(
                "return arguments[0].filteredItems", comboBox);

        items.forEach(item -> {
            Assert.assertNotNull(item.get("key"));
            Assert.assertNotNull(item.get("label"));
        });

        Map<String, ?> firstItem = items.get(0);
        Assert.assertEquals("A V Club Disagrees", firstItem.get("label"));

        executeScript(
                "arguments[0].selectedItem = arguments[0].filteredItems[1]",
                comboBox);

        waitUntil(
                driver -> message.getText().equals("Selected artist: Haywyre"));

        executeScript(
                "arguments[0].selectedItem = arguments[0].filteredItems[0]",
                comboBox);

        waitUntil(driver -> message.getText().equals(
                "Selected artist: Haircuts for Men\nThe old selection was: Haywyre"));
    }

    @Test
    public void inMemoryLazyComboBox_itemsLoadedLazily() {
        testLazyComboBox("lazy-loading-box");
    }

    @Test
    public void callBackLazyComboBox_itemsLoadedLazily() {
        testLazyComboBox("callback-box");
    }

    private void testLazyComboBox(String comboBoxId) {
        openTabAndCheckForErrors("lazy-loading");
        ComboBoxElement comboBox = $(ComboBoxElement.class).id(comboBoxId);

        Assert.assertEquals("No items should be loaded initially.", 0,
                getLoadedItems(comboBox).size());

        comboBox.openPopup();

        Assert.assertEquals(
                "First page should be loaded after opening overlay.", 50,
                getLoadedItems(comboBox).size());
        assertRendered();

        scrollToItem(comboBox, 50);
        Assert.assertEquals("Second page should be loaded after scrolling.",
                100, getLoadedItems(comboBox).size());
    }

    private void scrollToItem(ComboBoxElement comboBox, int index) {
        executeScript("arguments[0].$.overlay._scrollIntoView(arguments[1])",
                comboBox, index);
    }

    private List<JsonObject> getLoadedItems(ComboBoxElement comboBox) {
        List<JsonObject> list = (List<JsonObject>) executeScript(
                "return arguments[0].filteredItems.filter("
                        + "item => !(item instanceof Vaadin.ComboBoxPlaceholder));",
                comboBox);
        return list;
    }

    private void assertRendered() {
        List<String> items = $("vaadin-combo-box-overlay").first().$("div")
                .id("content").$("vaadin-combo-box-item").all().stream()
                .map(element -> element.$("div").id("content")
                        .getPropertyString("innerHTML"))
                .collect(Collectors.toList());
        Assert.assertTrue("Expected more than 10 items to be rendered.",
                items.size() > 10);
        items.forEach(item -> {
            boolean containsAtLeastTwoWords = Pattern.matches("\\S+\\s\\S+.*",
                    item);
            Assert.assertTrue(
                    "Expected rendred item to contain at least two words, but was: "
                            + item,
                    containsAtLeastTwoWords);
        });
    }

    @Override
    protected String getTestPath() {
        return ("/vaadin-combo-box");
    }
}
