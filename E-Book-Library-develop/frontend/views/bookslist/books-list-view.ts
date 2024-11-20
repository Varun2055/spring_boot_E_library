import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout';
import '@vaadin/vaadin-select';
import { applyTheme } from 'Frontend/generated/theme';
import { html, LitElement } from 'lit';
import { customElement } from 'lit/decorators.js';

@customElement('books-list-view')
export class BooksListView extends LitElement {
  connectedCallback() {
    super.connectedCallback();
    // Apply the theme manually because of https://github.com/vaadin/flow/issues/11160
    applyTheme(this.renderRoot);
  }

  render() {
    return html`
      <main class="max-w-screen-xl mx-auto pb-l px-l">
        <vaadin-horizontal-layout class="items-center justify-between">
          <vaadin-vertical-layout class="m-2" style="margin-right:10px">
            <h2 class="mb-0 mt-xl text-3xl mr-2">Books Library</h2>
           <vaadin-select label="Filter" id="filter"></vaadin-select>
          </vaadin-vertical-layout>
          <vaadin-button id="addBookBtn">Add Book</vaadin-button>
        </vaadin-horizontal-layout>
        <ol class="gap-m inline-flex list-none m-0 p-0 flex-wrap" style="justify-content:space-between">
          <slot></slot>
        </ol>
      </main>
    `;
  }
}
