/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.formulaeditor;

import android.test.AndroidTestCase;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.SingleSprite;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;

import java.util.ArrayList;
import java.util.List;

public class DataContainerTest extends AndroidTestCase {

	private static final String PROJECT_USER_LIST_NAME_2 = "project_user_list_2";
	private static final String PROJECT_USER_LIST_NAME = "project_user_list";
	private static final String PROJECT_USER_VARIABLE_NAME_2 = "project_user_variable_2";
	private static final String PROJECT_USER_VARIABLE_NAME = "project_user_variable";
	private static final String SPRITE_USER_LIST_NAME = "sprite_user_list";
	private Sprite firstSprite;

	private static final List<Object> USER_LIST_VALUES_SINGLE_NUMBER_STRING = new ArrayList<Object>();
	static {
		USER_LIST_VALUES_SINGLE_NUMBER_STRING.add("123345456");
	}

	private static final List<Object> USER_LIST_VALUES_MULTIPLE_NUMBER_STRING = new ArrayList<Object>();
	static {
		USER_LIST_VALUES_MULTIPLE_NUMBER_STRING.add("1");
		USER_LIST_VALUES_MULTIPLE_NUMBER_STRING.add("2");
		USER_LIST_VALUES_MULTIPLE_NUMBER_STRING.add("3");
	}

	private static final List<Object> USER_LIST_VALUES_MULTIPLE_NUMBERS = new ArrayList<Object>();
	static {
		USER_LIST_VALUES_MULTIPLE_NUMBERS.add(1.0);
		USER_LIST_VALUES_MULTIPLE_NUMBERS.add(2.0);
		USER_LIST_VALUES_MULTIPLE_NUMBERS.add(3.0);
	}

	private static final List<Object> USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER = new ArrayList<Object>();
	static {
		USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER.add(1.0);
		USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER.add("2");
		USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER.add(3.0);
		USER_LIST_VALUES_MULTIPLE_NUMBERS_STRING_INTEGER.add("4");
	}

	private static final List<Object> USER_LIST_VALUES_STRINGS_AND_NUMBERS = new ArrayList<Object>();
	private DataContainer dataContainer;
	static {
		USER_LIST_VALUES_STRINGS_AND_NUMBERS.add("Hello");
		USER_LIST_VALUES_STRINGS_AND_NUMBERS.add(42.0);
		USER_LIST_VALUES_STRINGS_AND_NUMBERS.add("WORLDS");
	}
	@Override
	protected void setUp() {
		Project project = new Project(null, "testProject");
		firstSprite = new SingleSprite("firstSprite");
		StartScript startScript = new StartScript();
		ChangeSizeByNBrick changeBrick = new ChangeSizeByNBrick(10);
		firstSprite.addScript(startScript);
		startScript.addBrick(changeBrick);
		project.getDefaultScene().addSprite(firstSprite);
		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(firstSprite);

		dataContainer = ProjectManager.getInstance().getCurrentScene().getDataContainer();
		dataContainer.addProjectUserList(PROJECT_USER_LIST_NAME);
		dataContainer.addSpriteUserListToSprite(firstSprite, SPRITE_USER_LIST_NAME);
		dataContainer.addProjectUserList(PROJECT_USER_LIST_NAME_2);
	}

	public void testGetUserList() {

		ProjectManager.getInstance().getCurrentScene().getDataContainer().deleteUserListByName(PROJECT_USER_LIST_NAME);
		ProjectManager.getInstance().getCurrentScene().getDataContainer().deleteUserListByName(PROJECT_USER_LIST_NAME_2);
		ProjectManager.getInstance().getCurrentScene().getDataContainer().deleteUserListByName(SPRITE_USER_LIST_NAME);

		assertNull("UserList found, but should not!", ProjectManager.getInstance().getCurrentScene().getDataContainer().getUserList());

		dataContainer.addProjectUserList(PROJECT_USER_LIST_NAME);
		dataContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite).setList(USER_LIST_VALUES_MULTIPLE_NUMBERS);

		dataContainer.addProjectUserList(PROJECT_USER_LIST_NAME);
		dataContainer.getUserList(PROJECT_USER_LIST_NAME, firstSprite).setList(USER_LIST_VALUES_SINGLE_NUMBER_STRING);

		UserList userList = ProjectManager.getInstance().getCurrentScene().getDataContainer().getUserList();
		assertEquals("getUserList returned wrong UserList values!", USER_LIST_VALUES_SINGLE_NUMBER_STRING, userList.getList());
	}

	public void testRenameListGlobal() {
		ProjectManager.getInstance().getCurrentScene().getDataContainer().deleteUserListByName(PROJECT_USER_LIST_NAME_2);
		dataContainer.addProjectUserList(PROJECT_USER_LIST_NAME);

		dataContainer.renameProjectUserList(PROJECT_USER_LIST_NAME_2, PROJECT_USER_LIST_NAME);

		UserList userList = ProjectManager.getInstance().getCurrentScene().getDataContainer().findUserList(
				PROJECT_USER_LIST_NAME_2, dataContainer.getProjectLists());
		assertEquals("rename list value failed!", userList.getName(), PROJECT_USER_LIST_NAME_2);
	}

	public void testRenameListLocal() {
		ProjectManager.getInstance().getCurrentScene().getDataContainer().deleteUserListByName(PROJECT_USER_LIST_NAME_2);
		dataContainer.addSpriteUserList(PROJECT_USER_LIST_NAME);

		dataContainer.renameSpriteUserList(PROJECT_USER_LIST_NAME_2, PROJECT_USER_LIST_NAME);

		UserList userList = ProjectManager.getInstance().getCurrentScene().getDataContainer().findUserList(
				PROJECT_USER_LIST_NAME_2, dataContainer.getSpriteListOfLists(firstSprite));
		assertEquals("rename list value failed!", userList.getName(), PROJECT_USER_LIST_NAME_2);
	}

	public void testRenameVariableGlobal() {
		dataContainer.addProjectUserVariable(PROJECT_USER_VARIABLE_NAME);

		dataContainer.renameProjectUserVariable(PROJECT_USER_VARIABLE_NAME_2, PROJECT_USER_VARIABLE_NAME);

		UserVariable userVariable = ProjectManager.getInstance().getCurrentScene().getDataContainer().findUserVariable(
				PROJECT_USER_VARIABLE_NAME_2, dataContainer.getProjectVariables());
		assertEquals("rename variable failed!", userVariable.getName(), PROJECT_USER_VARIABLE_NAME_2);
	}

	public void testRenameVariableLocal() {
		dataContainer.addSpriteUserVariable(PROJECT_USER_VARIABLE_NAME);

		dataContainer.renameSpriteUserVariable(PROJECT_USER_VARIABLE_NAME_2, PROJECT_USER_VARIABLE_NAME);

		UserVariable userVariable = ProjectManager.getInstance().getCurrentScene().getDataContainer().findUserVariable(
				PROJECT_USER_VARIABLE_NAME_2, dataContainer.getVariableListForSprite(firstSprite));
		assertEquals("rename variable failed!", userVariable.getName(), PROJECT_USER_VARIABLE_NAME_2);
	}
}
