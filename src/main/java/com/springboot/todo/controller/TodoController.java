package com.springboot.todo.controller;

import com.springboot.todo.model.Activity;
import com.springboot.todo.model.CheckList;
import com.springboot.todo.model.DashBoard;
import com.springboot.todo.model.Todo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.todo.repository.TodoRepoImpl;

/**
 * Controller class to perform CRUD operations on Todo dashboard
 * 
 * @author isriniva
 *
 */
@RestController
@RequestMapping("/dashboard")
public class TodoController {

	@Autowired
	private TodoRepoImpl todoRepoImpl;

	/**
	 * Retrieve all the dashboards
	 * 
	 * @return List of dashboards if retrieved from database
	 */
	@GetMapping("/all")
	public ResponseEntity<List<DashBoard>> retrieveAllDashBoards() {
		return new ResponseEntity<List<DashBoard>>(todoRepoImpl.findAll(), HttpStatus.OK);
	}

	/**
	 * Retrieve the dashboard by its name
	 * 
	 * @param name
	 *            the name of the dashboard
	 * @return Dashboard if the name given is already present in database
	 */
	@GetMapping("/getSingle/{name}")
	public ResponseEntity<DashBoard> retrieveTodo(@PathVariable String name) {
		return new ResponseEntity<DashBoard>(todoRepoImpl.findByName(name), HttpStatus.OK);
	}

	/**
	 * Add a dashboard based on the JSON given
	 * 
	 * @param dashboard
	 *            Dashboard object that contain all information
	 * @return Status CREATED if the post operation is success along with the
	 *         dashboard that we created
	 */
	@PostMapping("/saveSingle")
	public ResponseEntity<DashBoard> addTodo(@RequestBody DashBoard dashboard) {
		todoRepoImpl.save(dashboard);
		return new ResponseEntity<DashBoard>(dashboard, HttpStatus.CREATED);
	}

	/**
	 * Add/Update/Delete users for a dashboard based on the JSON given
	 * 
	 * @param id,users
	 *            array the name of the dashboard list of users
	 * @return Status OK if the put operation is success along with the dashboard
	 *         that we updated
	 */
	@PutMapping("/{id}/addUser")
	public ResponseEntity<DashBoard> addUser(@PathVariable String id, @RequestBody String[] users) {
		DashBoard dashBoard = todoRepoImpl.findByName(id);
		dashBoard.setUsers(users);
		todoRepoImpl.save(dashBoard);
		return new ResponseEntity<DashBoard>(dashBoard, HttpStatus.OK);

	}

	/**
	 * Add todo for a dashboard based on the JSON given
	 * 
	 * @param id,Todo
	 *            the name of the dashboard Todo object that contains all
	 *            information
	 * @return Status OK if the put operation is success along with the dashboard
	 *         that we updated
	 */
	@PutMapping("/{id}/addTodo")
	public ResponseEntity<DashBoard> addTodo(@PathVariable String id, @RequestBody Todo todo) {
		DashBoard dashBoard = todoRepoImpl.findByName(id);
		List<Todo> todoList = dashBoard.getTodo();
		todoList.add(todo);
		dashBoard.setTodo(todoList);
		todoRepoImpl.save(dashBoard);
		return new ResponseEntity<DashBoard>(dashBoard, HttpStatus.OK);
	}

	/**
	 * Add Checklist for a Todo based on the JSON given
	 * 
	 * @param id,todoId,checkList
	 *            the name of the dashboard Id of Todo CheckList object that
	 *            contains subtask information
	 * @return Status OK if the put operation is success along with the dashboard
	 *         that we updated
	 */
	@PutMapping("{id}/{todoId}/addChecklist")
	public ResponseEntity<DashBoard> addCheckListForTodo(@PathVariable String id, @PathVariable String todoId,
			@RequestBody CheckList checkList) {
		DashBoard dashBoard = todoRepoImpl.findByName(id);
		List<Todo> todoList = dashBoard.getTodo();
		todoList.forEach(todo -> {
			if (todo.getId().equals(todoId)) {
				List<CheckList> list = todo.getCheckList();
				list.add(checkList);
				todo.setCheckList(list);
			}
		});

		dashBoard.setTodo(todoList);
		todoRepoImpl.save(dashBoard);
		return new ResponseEntity<DashBoard>(dashBoard, HttpStatus.OK);
	}

	/**
	 * Update Checklist status for a particular subTask in a todo based on the JSON
	 * given
	 * 
	 * @param id,todoId,checkList
	 *            the name of the dashboard Id of Todo CheckList object that
	 *            contains subtask information
	 * @return Status OK if the put operation is success along with the dashboard
	 *         that we updated
	 */
	@PutMapping("{id}/{todoId}/updateChecklistStatus")
	public ResponseEntity<DashBoard> updateCheckListStatus(@PathVariable String id, @PathVariable String todoId,
			@RequestBody CheckList checkList) {
		DashBoard dashBoard = todoRepoImpl.findByName(id);
		List<Todo> todoList = dashBoard.getTodo();
		todoList.forEach(todo -> {
			if (todo.getId().equals(todoId)) {
				List<CheckList> list = todo.getCheckList();
				list.forEach(checklist -> {
					if (checklist.getSubTask().equals(checkList.getSubTask())) {
						checklist.setCheckListStatus(checkList.getCheckListStatus());
					}
				});

				todo.setCheckList(list);
			}
		});

		dashBoard.setTodo(todoList);
		todoRepoImpl.save(dashBoard);
		return new ResponseEntity<DashBoard>(dashBoard, HttpStatus.OK);
	}

	/**
	 * Add Activity for a todo in a dashboard based on the JSON given
	 * 
	 * @param id,todoId,activity
	 *            the name of the dashboard Id of Todo activity object that contains
	 *            an entry of activity
	 * @return Status OK if the put operation is success along with the dashboard
	 *         that we updated
	 */
	@PutMapping("{id}/{todoId}/addActivity")
	public ResponseEntity<DashBoard> addActivityForTodo(@PathVariable String id, @PathVariable String todoId,
			@RequestBody Activity activity) {
		DashBoard dashBoard = todoRepoImpl.findByName(id);
		List<Todo> todoList = dashBoard.getTodo();
		todoList.forEach(todo -> {
			if (todo.getId().equals(todoId)) {
				List<Activity> list = todo.getActivityList();
				list.add(activity);
				todo.setActivityList(list);
			}
		});

		dashBoard.setTodo(todoList);
		todoRepoImpl.save(dashBoard);
		return new ResponseEntity<DashBoard>(dashBoard, HttpStatus.OK);
	}

	/**
	 * Add Activity for a todo in a dashboard based on the JSON given
	 * 
	 * @param id,todoId,Todo
	 *            the name of the dashboard Id of Todo todo object that contains
	 *            duedate value to be updated
	 * @return Status OK if the put operation is success along with the dashboard
	 *         that we updated
	 */
	@PutMapping("{id}/{todoId}/changeDueDate")
	public ResponseEntity<DashBoard> updateTodoDueDate(@PathVariable String id, @PathVariable String todoId,
			@RequestBody Todo todoReq) {
		DashBoard dashBoard = todoRepoImpl.findByName(id);
		List<Todo> todoList = dashBoard.getTodo();
		todoList.forEach(todo -> {
			if (todo.getId().equals(todoId)) {
				todo.setDueDate(todoReq.getDueDate());
			}
		});

		dashBoard.setTodo(todoList);
		todoRepoImpl.save(dashBoard);
		return new ResponseEntity<DashBoard>(dashBoard, HttpStatus.OK);
	}

	/**
	 * Update Phase of a todo in a dashboard based on the JSON given
	 * 
	 * @param id,todoId,Todo
	 *            the name of the dashboard Id of Todo todo object that contains
	 *            Phase to be updated
	 * @return Status OK if the put operation is success along with the dashboard
	 *         that we updated
	 */
	@PutMapping("{id}/{todoId}/changeTodoPhase")
	public ResponseEntity<DashBoard> updateTodoPhase(@PathVariable String id, @PathVariable String todoId,
			@RequestBody Todo todoReq) {
		DashBoard dashBoard = todoRepoImpl.findByName(id);
		List<Todo> todoList = dashBoard.getTodo();
		todoList.forEach(todo -> {
			if (todo.getId().equals(todoId)) {
				todo.setPhase(todoReq.getPhase());
			}
		});

		dashBoard.setTodo(todoList);
		todoRepoImpl.save(dashBoard);
		return new ResponseEntity<DashBoard>(dashBoard, HttpStatus.OK);
	}

}
