/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.formulaeditor;

import android.test.AndroidTestCase;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.formulaeditor.Functions;
import org.catrobat.catroid.formulaeditor.InternFormulaParser;
import org.catrobat.catroid.formulaeditor.InternToken;
import org.catrobat.catroid.formulaeditor.InternTokenType;
import org.catrobat.catroid.formulaeditor.Operators;
import org.catrobat.catroid.formulaeditor.UserListContainer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParserTestUserLists extends AndroidTestCase {

	private static final String PROJECT_USER_LIST_NAME_2 = "project_user_list_2";
	private static final String PROJECT_USER_LIST_NAME = "project_user_list";
	private static final String SPRITE_USER_LIST_NAME = "sprite_user_list";
	private Project project;
	private Sprite firstSprite;
	private StartScript startScript;
	private ChangeSizeByNBrick changeBrick;
	private static final Double DELTA = 0.01;

	private static final Double EMPTY_USER_LIST_INTERPRETATION_VALUE = 0d;

	private static final Double USER_LIST_VALUES_SINGLE_NUMBER_STRING_INTERPRETATION_VALUE = 1.0;
	private static final List<Object> USER_LIST_VALUES_SINGLE_NUMBER_STRING = new ArrayList<Object>();
	static {
		USER_LIST_VALUES_SINGLE_NUMBER_STRING.add("1");
	}

	private static final String USER_LIST_VALUES_MULTIPLE_NUMBER_STRING_INTERPRETATION_VALUE = "123";
	private static final List<Object> USER_LIST_VALUES_MULTIPLE_NUMBER_STRING = new ArrayList<Object>();
	static {
		USER_LIST_VALUES_MULTIPLE_NUMBER_STRING.add("1");
		USER_LIST_VALUES_MULTIPLE_NUMBER_STRING.add("2");
		USER_LIST_VALUES_MULTIPLE_NUMBER_STRING.add("3");
	}

	private static final String USER_LIST_VALUES_MULTIPLE_NUMBERS_INTERPRETATION_VALUE = "123";
	private static final List<Object> USER_LIST_VALUES_MULTIPLE_NUMBERS = new ArrayList<Object>();
	static {
		USER_LIST_VALUES_MULTIPLE_NUMBERS.add(1.0);
		USER_LIST_VALUES_MULTIPLE_NUMBERS.add(2.0);
		USER_LIST_VALUES_MULTIPLE_NUMBERS.add(3.0);
	}

	private static final String USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER_INTERPRETATION_VALUE = "1234";
	private static final List<Object> USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER = new ArrayList<Object>();
	static {
		USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER.add(1.0);
		USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER.add("2");
		USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER.add(3.0);
		USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER.add("4");
	}

	private static final String USER_LIST_VALUES_STRINGS_AND_NUMBERS_INTERPRETATION_VALUE = "Hello 42.0 WORLDS";
	private static final List<Object> USER_LIST_VALUES_STRINGS_AND_NUMBERS = new ArrayList<Object>();
	private UserListContainer userListContainer;
	static {
		USER_LIST_VALUES_STRINGS_AND_NUMBERS.add("Hello");
		USER_LIST_VALUES_STRINGS_AND_NUMBERS.add(42.0);
		USER_LIST_VALUES_STRINGS_AND_NUMBERS.add("WORLDS");
	}

	@Override
	protected void setUp() {
		this.project = new Project(null, "testProject");
		firstSprite = new Sprite("firstSprite");
		startScript = new StartScript(firstSprite);
		changeBrick = new ChangeSizeByNBrick(firstSprite, 10);
		firstSprite.addScript(startScript);
		startScript.addBrick(changeBrick);
		project.addSprite(firstSprite);
		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(firstSprite);

		userListContainer = ProjectManager.getInstance().getCurrentProject().getUserLists();
		userListContainer.addProjectUserList(PROJECT_USER_LIST_NAME);
		userListContainer.addSpriteUserListToSprite(firstSprite, SPRITE_USER_LIST_NAME);
		userListContainer.addProjectUserList(PROJECT_USER_LIST_NAME_2);

	}

	public void testUserListInterpretationMultipleStringAndNumbers() {
		userListContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite).setList(
				USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER);
		assertEquals("Formula interpretation of List is not as expected",
				USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER_INTERPRETATION_VALUE,
				interpretUserList(PROJECT_USER_LIST_NAME));
	}

	public void testUserListInterpretationSingleNumberString() {
		userListContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite).setList(
				USER_LIST_VALUES_SINGLE_NUMBER_STRING);
		assertEquals("Formula interpretation of List is not as expected",
				USER_LIST_VALUES_SINGLE_NUMBER_STRING_INTERPRETATION_VALUE,
				(Double) interpretUserList(PROJECT_USER_LIST_NAME), DELTA);
	}

	public void testUserListInterpretationMultipleNumberString() {
		userListContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite).setList(
				USER_LIST_VALUES_MULTIPLE_NUMBER_STRING);
		assertEquals("Formula interpretation of List is not as expected",
				USER_LIST_VALUES_MULTIPLE_NUMBER_STRING_INTERPRETATION_VALUE, interpretUserList(PROJECT_USER_LIST_NAME));
	}

	public void testUserListInterpretationMultipleNumbers() {
		userListContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite).setList(USER_LIST_VALUES_MULTIPLE_NUMBERS);
		assertEquals("Formula interpretation of List is not as expected",
				USER_LIST_VALUES_MULTIPLE_NUMBERS_INTERPRETATION_VALUE, interpretUserList(PROJECT_USER_LIST_NAME));
	}

	public void testUserListInterpretationStringsAndNumbers() {
		userListContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite)
				.setList(USER_LIST_VALUES_STRINGS_AND_NUMBERS);
		assertEquals("Formula interpretation of List is not as expected",
				USER_LIST_VALUES_STRINGS_AND_NUMBERS_INTERPRETATION_VALUE, interpretUserList(PROJECT_USER_LIST_NAME));
	}

	public void testUserListInterpretationEmptyList() {
		userListContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite).getList().clear();

		assertEquals("Formula interpretation of List is not as expected", EMPTY_USER_LIST_INTERPRETATION_VALUE,
				(Double) interpretUserList(PROJECT_USER_LIST_NAME), DELTA);
	}

	public void testUserListReset() {
		userListContainer.addSpriteUserList(SPRITE_USER_LIST_NAME);
		userListContainer.addSpriteUserList(PROJECT_USER_LIST_NAME_2);
		userListContainer.addSpriteUserList(PROJECT_USER_LIST_NAME);

		userListContainer.getUserList(SPRITE_USER_LIST_NAME, firstSprite).setList(USER_LIST_VALUES_MULTIPLE_NUMBERS);
		userListContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite).setList(USER_LIST_VALUES_MULTIPLE_NUMBERS);
		userListContainer.getUserList(PROJECT_USER_LIST_NAME_2, firstSprite).setList(USER_LIST_VALUES_MULTIPLE_NUMBERS);

		userListContainer.resetAllUserLists();

		assertEquals("Sprite UserList did not reset", EMPTY_USER_LIST_INTERPRETATION_VALUE,
				interpretUserList(SPRITE_USER_LIST_NAME));
		assertEquals("Project UserList did not reset", EMPTY_USER_LIST_INTERPRETATION_VALUE,
				interpretUserList(PROJECT_USER_LIST_NAME));
		assertEquals("Project UserList 2 did not reset", EMPTY_USER_LIST_INTERPRETATION_VALUE,
				interpretUserList(PROJECT_USER_LIST_NAME_2));
	}

	public void testNotExistingUserList() {
		List<InternToken> internTokenList = new LinkedList<InternToken>();
		internTokenList.add(new InternToken(InternTokenType.USER_LIST, "NOT_EXISTING_USER_LIST"));
		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTree = internParser.parseFormula();

		assertNull("Invalid user list parsed:   NOT_EXISTING_USER_LIST)", parseTree);
		int errorTokenIndex = internParser.getErrorTokenIndex();
		assertEquals("Error Token Index is not as expected", 0, errorTokenIndex);
	}

	public void testFunctionListItem(){
		userListContainer.addSpriteUserList(PROJECT_USER_LIST_NAME);
		userListContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite).setList(USER_LIST_VALUES_MULTIPLE_NUMBERS);

		String index = "1";
		FormulaEditorUtil.testDoubleParameterFunction(Functions.LIST_ITEM, InternTokenType.NUMBER, index,
				InternTokenType.USER_LIST, PROJECT_USER_LIST_NAME, 1.0,
				firstSprite);

		index = "0";
		FormulaEditorUtil.testDoubleParameterFunction(Functions.LIST_ITEM, InternTokenType.NUMBER, index,
				InternTokenType.USER_LIST, PROJECT_USER_LIST_NAME, "",
				firstSprite);

		index = "4";
		FormulaEditorUtil.testDoubleParameterFunction(Functions.LIST_ITEM, InternTokenType.NUMBER, index,
				InternTokenType.USER_LIST, PROJECT_USER_LIST_NAME, "",
				firstSprite);

		index = "1.4";
		FormulaEditorUtil.testDoubleParameterFunction(Functions.LIST_ITEM, InternTokenType.NUMBER, index,
				InternTokenType.USER_LIST, PROJECT_USER_LIST_NAME, 1.0,
				firstSprite);

		index = "1.0";
		FormulaEditorUtil.testDoubleParameterFunction(Functions.LIST_ITEM, InternTokenType.STRING, index,
				InternTokenType.USER_LIST, PROJECT_USER_LIST_NAME, 1.0,
				firstSprite);

	}

	public void testLength() {
		userListContainer.addProjectUserList(PROJECT_USER_LIST_NAME);

		FormulaEditorUtil.testSingleParameterFunction(Functions.LENGTH, InternTokenType.USER_LIST, PROJECT_USER_LIST_NAME,
				(double) 0, firstSprite);

		userListContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite).setList(USER_LIST_VALUES_MULTIPLE_NUMBERS);
		FormulaEditorUtil.testSingleParameterFunction(Functions.LENGTH, InternTokenType.USER_LIST, PROJECT_USER_LIST_NAME,
				(double) USER_LIST_VALUES_MULTIPLE_NUMBERS.size(), firstSprite);

	}

	private Object interpretUserList(String userListName) {
		List<InternToken> internTokenList = new LinkedList<InternToken>();
		internTokenList.add(new InternToken(InternTokenType.USER_LIST, userListName));
		InternFormulaParser internParser = new InternFormulaParser(internTokenList);
		FormulaElement parseTree = internParser.parseFormula();
		Formula userVariableFormula = new Formula(parseTree);

		return userVariableFormula.interpretObject(firstSprite);
	}

}
