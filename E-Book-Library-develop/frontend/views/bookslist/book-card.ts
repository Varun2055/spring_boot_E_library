import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout';
import '@vaadin/vaadin-select';
import { html, LitElement } from 'lit';
import { customElement } from 'lit/decorators.js';

@customElement('book-card')
export class BookCard extends LitElement {
	createRenderRoot() {
		// Do not use a shadow root
		return this;
	}

	render() {
		return html`<li class="bg-contrast-5 flex flex-col items-start p-m rounded-l" style="hegith:40rem; width:20rem">
	<vaadin-button id="delete" class="v-button-link"></vaadin-button>
	<div class="bg-contrast flex items-center justify-center mb-m overflow-hidden rounded-m w-full"
		style="height: 430px;">
		<img id="image" class="w-full" />
	</div>
	<vaadin-horizontal-layout class="justify-between w-full">
		<vaadin-vertical-layout class="m-2">
			<a href="#" class="text-xl font-semibold" id="header"></a>
			<span class="text-s text-secondary" id="subtitle"></span>
		</vaadin-vertical-layout>
		<vaadin-button id="favouriteBtn" class="v-button-link"></vaadin-button>
	</vaadin-horizontal-layout>
	<p class="my-m" id="text" style="height:160px;overflow:hidden;text-overflow:ellipsis"></p>
	<div id="badge"></div>
</li>`;
	}
}
