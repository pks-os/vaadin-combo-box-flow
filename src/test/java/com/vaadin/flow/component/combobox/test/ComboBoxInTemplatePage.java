/*
 * Copyright 2000-2018 Vaadin Ltd.
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

import java.util.Arrays;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.router.Route;

@Route("combo-box-in-template")
public class ComboBoxInTemplatePage extends Div {

    public ComboBoxInTemplatePage() {
        WrapperTemplate template = new WrapperTemplate();
        add(template);
        // ComboBox<String> combo = template.a.comboBox;
        initCombo(template.a.comboBox);
        // initCombo(template.b.comboBox);

        // initCombo(template.combo1);
        // initCombo(template.combo2);

        // initCombo(template.a.combo1);
        // initCombo(template.combo2);

    }

    private void addCombos() {

        ComboBoxInATemplate template = new ComboBoxInATemplate();
        add(template);
        ComboBox<String> combo = template.getComboBox();

        combo.setDataProvider(
                new ListDataProvider<>(Arrays.asList("1", "2", "3")));
        combo.setValue("1");
        combo.addValueChangeListener(e -> System.out.println(
                "Why am I automatically setting value to: " + e.getValue()));

        ComboBoxInATemplate2 template2 = new ComboBoxInATemplate2();
        add(template2);
        ComboBox<String> combo2 = template2.getComboBox();

        combo2.setDataProvider(
                new ListDataProvider<>(Arrays.asList("1", "2", "3")));
        combo2.setValue("1");
        combo2.addValueChangeListener(e -> {
            System.out.println(
                    "Why am I automatically setting value to: " + e.getValue());
        });

    }

    private void addCombo() {
        ComboBoxInATemplate template = new ComboBoxInATemplate();
        add(template);
        ComboBox<String> combo = template.getComboBox();
        initCombo(combo);
    }

    private void initCombo(ComboBox<String> combo) {

        combo.setDataProvider(
                new ListDataProvider<>(Arrays.asList("1", "2", "3")));
        combo.setValue("1");
        combo.addValueChangeListener(this::handler);
        combo.getElement().addEventListener("value-changed", this::handler2);
    }

    private void handler2(DomEvent domEvent) {
        System.out.println();
    }

    private void handler(HasValue.ValueChangeEvent e) {

        System.out.println(
                "Why am I automatically setting value to: " + e.getValue());
    }
}
