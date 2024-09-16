import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('WishList e2e test', () => {
  const wishListPageUrl = '/wish-list';
  const wishListPageUrlPattern = new RegExp('/wish-list(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const wishListSample = { title: 'what but evil' };

  let wishList;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/wish-lists+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/wish-lists').as('postEntityRequest');
    cy.intercept('DELETE', '/api/wish-lists/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (wishList) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/wish-lists/${wishList.id}`,
      }).then(() => {
        wishList = undefined;
      });
    }
  });

  it('WishLists menu should load WishLists page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('wish-list');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WishList').should('exist');
    cy.url().should('match', wishListPageUrlPattern);
  });

  describe('WishList page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(wishListPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WishList page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/wish-list/new$'));
        cy.getEntityCreateUpdateHeading('WishList');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wishListPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/wish-lists',
          body: wishListSample,
        }).then(({ body }) => {
          wishList = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/wish-lists+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [wishList],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(wishListPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WishList page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('wishList');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wishListPageUrlPattern);
      });

      it('edit button click should load edit WishList page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WishList');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wishListPageUrlPattern);
      });

      it('edit button click should load edit WishList page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WishList');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wishListPageUrlPattern);
      });

      it('last delete button click should delete instance of WishList', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('wishList').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wishListPageUrlPattern);

        wishList = undefined;
      });
    });
  });

  describe('new WishList page', () => {
    beforeEach(() => {
      cy.visit(`${wishListPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WishList');
    });

    it('should create an instance of WishList', () => {
      cy.get(`[data-cy="title"]`).type('following cassock');
      cy.get(`[data-cy="title"]`).should('have.value', 'following cassock');

      cy.get(`[data-cy="restricted"]`).should('not.be.checked');
      cy.get(`[data-cy="restricted"]`).click();
      cy.get(`[data-cy="restricted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        wishList = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', wishListPageUrlPattern);
    });
  });
});
